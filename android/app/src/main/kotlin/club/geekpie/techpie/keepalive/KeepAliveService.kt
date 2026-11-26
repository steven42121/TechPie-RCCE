package club.geekpie.techpie.keepalive

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import club.geekpie.techpie.MainActivity
import club.geekpie.techpie.R
import club.geekpie.techpie.island.FocusSequenceStore
import club.geekpie.techpie.island.IslandBackend
import club.geekpie.techpie.island.IslandFrame
import club.geekpie.techpie.island.IslandAlarms
import club.geekpie.techpie.island.IslandCapabilityProbe
import club.geekpie.techpie.island.IslandNotificationFactory

/** Whether the user has asked for a resident process this run. */
object KeepAlive {
    @Volatile
    var enabled: Boolean = false

    /**
     * A receiver woken by an alarm runs in a fresh process where [enabled] is
     * still false; ask the persisted preference instead, or the island would
     * forget that the user wants the process kept.
     */
    fun isEnabled(context: Context): Boolean =
        enabled || runCatching {
            context.getSharedPreferences("FlutterSharedPreferences", Context.MODE_PRIVATE)
                .getBoolean("flutter.keep_alive_enabled", false)
        }.getOrDefault(false)
}

/**
 * The opt-in resident process — the keep-alive chain the island used to run
 * unconditionally, now behind a settings switch.
 *
 * Its foreground notification is the island frame when there is one, and a
 * plain keep-alive notice when the island has nothing to say; either way the
 * process survives so the island timer and the band link keep working. Off by
 * default: a killed app keeps no residual process until the user asks for one.
 */
class KeepAliveService : Service() {

    private val factory by lazy {
        IslandNotificationFactory(this, FocusSequenceStore(this))
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val frameJson = intent?.getStringExtra(EXTRA_FRAME_JSON)
        val generation = intent?.takeIf { it.hasExtra(EXTRA_GENERATION) }
            ?.getLongExtra(EXTRA_GENERATION, -1L)
        val eventAt = intent?.takeIf { it.hasExtra(EXTRA_FRAME_AT) }
            ?.getLongExtra(EXTRA_FRAME_AT, -1L)
        if (frameJson != null && generation != null && eventAt != null &&
            !IslandAlarms.isCurrentFrame(this, generation, frameJson, eventAt)
        ) {
            return START_STICKY
        }
        render(
            frameJson,
            intent?.getStringExtra(EXTRA_BACKEND)
                ?.let { runCatching { IslandBackend.valueOf(it.uppercase()) }.getOrNull() },
        )
        return START_STICKY
    }

    private fun render(frameJson: String?, backend: IslandBackend?) {
        val frame = frameJson?.let { IslandFrame.fromJson(it) }
        val notification = if (frame == null) {
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_island_notification)
                .setContentTitle(getString(R.string.keepalive_notification_title))
                .setContentText(keepAliveNotificationText())
                .setContentIntent(contentIntent())
                .setOngoing(true)
                .setSilent(true)
                .build()
        } else {
            factory.build(
                frame,
                backend ?: IslandBackend.PLAIN,
                IslandNotificationFactory.NOTIFICATION_ID,
            )
        }
        startForeground(IslandNotificationFactory.NOTIFICATION_ID, notification)
    }

    private fun keepAliveNotificationText(): String = when (
        runCatching { IslandCapabilityProbe(this).snapshot().backend }
            .getOrDefault(IslandBackend.PLAIN)
    ) {
        IslandBackend.HYPER_ISLAND -> getString(R.string.keepalive_notification_text_hyper)
        IslandBackend.LIVE_UPDATE -> getString(R.string.keepalive_notification_text_live)
        IslandBackend.PLAIN -> getString(R.string.keepalive_notification_text_plain)
    }

    private fun contentIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                getString(R.string.keepalive_channel_name),
                NotificationManager.IMPORTANCE_LOW,
            ).apply {
                description = getString(R.string.keepalive_channel_desc)
                setShowBadge(false)
            },
        )
    }

    companion object {
        const val CHANNEL = "techpie/keep_alive"
        private const val ACTION_START = "club.geekpie.techpie.keepalive.action.START"
        private const val EXTRA_FRAME_JSON = "frame_json"
        private const val EXTRA_BACKEND = "backend"
        private const val EXTRA_GENERATION = "generation"
        private const val EXTRA_FRAME_AT = "frame_at"
        private const val CHANNEL_ID = "keep_alive"

        fun start(
            context: Context,
            frameJson: String? = null,
            backend: IslandBackend? = null,
            generation: Long? = null,
            frameAtMillis: Long? = null,
        ) {
            val appContext = context.applicationContext
            val intent = Intent(appContext, KeepAliveService::class.java).apply {
                action = ACTION_START
                if (frameJson != null) {
                    putExtra(EXTRA_FRAME_JSON, frameJson)
                    putExtra(EXTRA_BACKEND, backend?.name)
                    generation?.let { putExtra(EXTRA_GENERATION, it) }
                    frameAtMillis?.let { putExtra(EXTRA_FRAME_AT, it) }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                appContext.startForegroundService(intent)
            } else {
                appContext.startService(intent)
            }
        }

        fun stop(context: Context) {
            // stopService rather than startForegroundService: a service started
            // that way must call startForeground within five seconds, and the
            // stop path is exactly the one that would not.
            context.applicationContext.stopService(
                Intent(context.applicationContext, KeepAliveService::class.java),
            )
        }
    }
}

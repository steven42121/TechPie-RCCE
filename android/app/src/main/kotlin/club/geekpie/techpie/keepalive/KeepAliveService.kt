package club.geekpie.techpie.keepalive

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import club.geekpie.techpie.MainActivity
import club.geekpie.techpie.R
import club.geekpie.techpie.island.FocusSequenceStore
import club.geekpie.techpie.island.IslandBackend
import club.geekpie.techpie.island.IslandFrame
import club.geekpie.techpie.island.IslandNotificationFactory

/** Whether the user has asked for a resident process this run. */
object KeepAlive {
    @Volatile
    var enabled: Boolean = false
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
        render(
            intent?.getStringExtra(EXTRA_FRAME_JSON),
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
                .setContentText(getString(R.string.keepalive_notification_text))
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
        private const val CHANNEL_ID = "keep_alive"

        fun start(context: Context, frameJson: String? = null, backend: IslandBackend? = null) {
            val appContext = context.applicationContext
            val intent = Intent(appContext, KeepAliveService::class.java).apply {
                action = ACTION_START
                if (frameJson != null) {
                    putExtra(EXTRA_FRAME_JSON, frameJson)
                    putExtra(EXTRA_BACKEND, backend?.name)
                }
            }
            appContext.startForegroundService(intent)
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

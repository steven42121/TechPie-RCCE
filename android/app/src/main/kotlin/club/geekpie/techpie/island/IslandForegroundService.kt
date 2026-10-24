package club.geekpie.techpie.island

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

/**
 * Hosts the island notification as a foreground service so it survives process
 * death and can float to the island on MIUI. The frame is passed as JSON because
 * the Dart side owns all schedule state; this service only renders what it gets.
 */
class IslandForegroundService : Service() {

    private val factory by lazy { IslandNotificationFactory(this, FocusSequenceStore(this)) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> stopSelf()
            else -> {
                val frameJson = intent?.getStringExtra(EXTRA_FRAME_JSON).orEmpty()
                if (frameJson.isEmpty()) {
                    stopSelf()
                    return START_NOT_STICKY
                }
                val frame = IslandFrame.fromJson(frameJson)
                if (frame == null) {
                    stopSelf()
                    return START_NOT_STICKY
                }
                val backend = intent?.getStringExtra(EXTRA_BACKEND)
                    ?.let { runCatching { IslandBackend.valueOf(it.uppercase()) }.getOrNull() }
                    ?: IslandBackend.HYPER_ISLAND
                startForeground(
                    IslandNotificationFactory.NOTIFICATION_ID,
                    factory.build(frame, backend, IslandNotificationFactory.NOTIFICATION_ID),
                )
            }
        }
        return START_NOT_STICKY
    }

    companion object {
        const val ACTION_START = "club.geekpie.techpie.island.action.START"
        const val ACTION_STOP = "club.geekpie.techpie.island.action.STOP"
        private const val EXTRA_FRAME_JSON = "frame_json"
        private const val EXTRA_BACKEND = "backend"

        fun start(context: Context, frameJson: String, backend: IslandBackend) {
            context.startForegroundService(
                Intent(context, IslandForegroundService::class.java).apply {
                    action = ACTION_START
                    putExtra(EXTRA_FRAME_JSON, frameJson)
                    putExtra(EXTRA_BACKEND, backend.name)
                },
            )
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, IslandForegroundService::class.java))
        }
    }
}

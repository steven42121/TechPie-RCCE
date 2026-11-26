package club.geekpie.techpie.island

import android.content.Context
import club.geekpie.techpie.keepalive.KeepAlive
import club.geekpie.techpie.keepalive.KeepAliveService
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.json.JSONArray

/**
 * The schedule-island channel: capability probing, frame updates and dismissal.
 *
 * Registered per engine by whoever hosts one — the activity while the app is
 * open, and the background worker when it is not — so the island is never
 * painted by a process that has to stay alive to paint it.
 */
class IslandBridge(
    private val appContext: Context,
    messenger: BinaryMessenger,
    private val probe: IslandCapabilityProbe,
    private val factory: IslandNotificationFactory,
    private val requestPermission: (() -> Boolean)? = null,
) {
    private val channel = MethodChannel(messenger, CHANNEL)

    init {
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "probe" -> result.success(probe.snapshot().toMap())
                "refreshProbe" -> result.success(probe.refresh().toMap())
                "updateFrame" -> updateFrame(call, result)
                "dismiss" -> {
                    result.success(dismiss(call))
                }
                "scheduleFrames" -> scheduleFrames(call, result)
                "cancelFrames" -> {
                    IslandAlarms.cancelAll(appContext)
                    IslandAlarms.cancelRepaint(appContext)
                    result.success(true)
                }
                "requestPermission" -> result.success(requestPermission?.invoke() ?: false)
                else -> result.notImplemented()
            }
        }
    }

    private fun dismiss(call: MethodCall): Boolean {
        val generation = call.argument<Number>("generation")?.toLong()
            ?: IslandAlarms.currentGeneration(appContext)
        if (!IslandAlarms.recordCurrentFrame(
                appContext, generation, null, null, System.currentTimeMillis(),
            )
        ) {
            return false
        }
        IslandAlarms.cancelRepaint(appContext, generation)
        if (KeepAlive.enabled) {
            // The resident process is still wanted — for the band link — so the
            // island's slot falls back to the plain keep-alive notice.
            KeepAliveService.start(appContext)
        } else {
            factory.cancel(IslandNotificationFactory.NOTIFICATION_ID)
        }
        return true
    }

    /** Precomputed frames from the Dart side, posted by AlarmManager later. */
    private fun scheduleFrames(call: MethodCall, result: MethodChannel.Result) {
        val raw = call.argument<String>("frames") ?: "[]"
        val items = mutableListOf<IslandAlarms.Item>()
        try {
            val array = JSONArray(raw)
            for (i in 0 until array.length()) {
                val entry = array.getJSONObject(i)
                val frame = if (entry.isNull("frame")) {
                    null
                } else {
                    entry.getJSONObject("frame").toString()
                }
                items.add(IslandAlarms.Item(entry.getLong("at"), frame))
            }
        } catch (error: Exception) {
            result.error("BAD_FRAMES", error.message, null)
            return
        }
        val generation = IslandAlarms.schedule(appContext, items)
        result.success(generation)
    }

    fun dispose() {
        channel.setMethodCallHandler(null)
    }

    private fun updateFrame(call: MethodCall, result: MethodChannel.Result) {
        val frameJson = call.argument<String>("frame")
        val frame = frameJson?.let { IslandFrame.fromJson(it) }
        if (frame == null) {
            result.error("BAD_FRAME", "missing or unreadable frame json", null)
            return
        }
        // The native probe is authoritative. Keep the backend choice tied to
        // the device's actual capability so callers cannot force HyperOS
        // focus extras on a device that does not support them.
        val backend = probe.snapshot().backend
        try {
            val generation = call.argument<Number>("generation")?.toLong()
                ?: IslandAlarms.currentGeneration(appContext)
            val eventAt = System.currentTimeMillis()
            if (!IslandAlarms.recordCurrentFrame(
                    appContext, generation, frameJson, frame.expiresAt, eventAt,
                )
            ) {
                result.error("STALE_FRAME", "A newer island frame is already active", null)
                return
            }
            if (KeepAlive.enabled) {
                // The keep-alive chain: the frame is posted as the foreground
                // notification, so painting it is also what holds the process
                // open for the band link and the next frame.
                KeepAliveService.start(
                    appContext, frameJson, backend, generation, eventAt,
                )
            } else {
                factory.updateFrame(frame, backend, IslandNotificationFactory.NOTIFICATION_ID)
            }
            // Keep a running class on the island even when the focus slot is
            // taken by another app's notification between frames.
            IslandAlarms.scheduleRepaint(appContext, frameJson, generation)
            result.success(backend.name.lowercase())
        } catch (e: Exception) {
            result.error("ISLAND_ERROR", e.message, null)
        }
    }

    companion object {
        const val CHANNEL = "techpie/island"
    }
}

package club.geekpie.techpie.island

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * The schedule-island channel: capability probing, frame updates and dismissal.
 *
 * Registered per engine by whoever hosts one — the activity while the app is
 * open, and the background worker when it is not — so the island is never
 * painted by a process that has to stay alive to paint it.
 */
class IslandBridge(
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
                    factory.cancel(IslandNotificationFactory.NOTIFICATION_ID)
                    result.success(true)
                }
                "requestPermission" -> result.success(requestPermission?.invoke() ?: false)
                else -> result.notImplemented()
            }
        }
    }

    fun dispose() {
        channel.setMethodCallHandler(null)
    }

    private fun updateFrame(call: MethodCall, result: MethodChannel.Result) {
        val frame = call.argument<String>("frame")?.let { IslandFrame.fromJson(it) }
        if (frame == null) {
            result.error("BAD_FRAME", "missing or unreadable frame json", null)
            return
        }
        // The caller may force a tier for testing; by default the device
        // decides, so a phone without the focus grant still gets the capsule
        // or a plain ongoing notification instead of nothing.
        val backend = call.argument<String>("backend")
            ?.let { name -> runCatching { IslandBackend.valueOf(name.uppercase()) }.getOrNull() }
            ?: probe.snapshot().backend
        try {
            factory.updateFrame(frame, backend, IslandNotificationFactory.NOTIFICATION_ID)
            result.success(backend.name.lowercase())
        } catch (e: Exception) {
            result.error("ISLAND_ERROR", e.message, null)
        }
    }

    companion object {
        const val CHANNEL = "techpie/island"
    }
}

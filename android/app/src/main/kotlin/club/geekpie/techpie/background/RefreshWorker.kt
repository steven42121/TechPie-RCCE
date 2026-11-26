package club.geekpie.techpie.background

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import club.geekpie.techpie.island.FocusSequenceStore
import club.geekpie.techpie.island.IslandBridge
import club.geekpie.techpie.island.IslandCapabilityProbe
import club.geekpie.techpie.island.IslandNotificationFactory
import club.geekpie.techpie.notification.SpendingNotificationBridge
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.TimeUnit

/**
 * Wakes the Dart side briefly to refresh the schedule island and to report new
 * card spending, then destroys the engine and returns. Nothing stays resident:
 * a killed app keeps no process, and the next scheduled run brings its own.
 */
class RefreshWorker(appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    override fun doWork(): Result {
        val islandOnly = inputData.getBoolean(ISLAND_ONLY_KEY, false)
        val main = Handler(Looper.getMainLooper())
        val finished = CountDownLatch(1)
        val completed = AtomicBoolean(false)
        // Engine and channel APIs require the main thread; the Worker waits on
        // its background thread so Flutter can initialize and send "done".
        var engine: FlutterEngine? = null
        var island: IslandBridge? = null
        var spending: SpendingNotificationBridge? = null
        var completionChannel: MethodChannel? = null
        main.post {
            try {
                if (isStopped) {
                    finished.countDown()
                    return@post
                }
                val loader = FlutterInjector.instance().flutterLoader()
                loader.startInitialization(applicationContext)
                loader.ensureInitializationComplete(applicationContext, null)
                val runningEngine = FlutterEngine(applicationContext)
                engine = runningEngine
                val messenger = runningEngine.dartExecutor.binaryMessenger
                completionChannel = MethodChannel(messenger, CHANNEL).also { channel ->
                    channel.setMethodCallHandler { call, result ->
                        if (call.method == "done") {
                            result.success(true)
                            completed.set(true)
                            finished.countDown()
                        } else {
                            result.notImplemented()
                        }
                    }
                }
                island = IslandBridge(
                    applicationContext, messenger, IslandCapabilityProbe(applicationContext),
                    IslandNotificationFactory(applicationContext, FocusSequenceStore(applicationContext)),
                )
                if (!islandOnly) {
                    spending = SpendingNotificationBridge(applicationContext, messenger)
                }
                runningEngine.dartExecutor.executeDartEntrypoint(
                    DartExecutor.DartEntrypoint(
                        loader.findAppBundlePath(),
                        entrypointFor(islandOnly),
                    ),
                )
            } catch (_: Exception) {
                finished.countDown()
            }
        }
        try {
            finished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        } finally {
            main.post {
                completionChannel?.setMethodCallHandler(null)
                island?.dispose()
                spending?.dispose()
                engine?.destroy()
            }
        }
        return if (completed.get()) Result.success() else Result.retry()
    }

    companion object {
        const val CHANNEL = "techpie/background"
        const val ENTRYPOINT = "backgroundRefresh"
        const val ISLAND_ENTRYPOINT = "islandTimelineRebuild"
        const val ISLAND_ONLY_KEY = "island_only"
        private const val TIMEOUT_SECONDS = 60L

        internal fun entrypointFor(islandOnly: Boolean): String =
            if (islandOnly) ISLAND_ENTRYPOINT else ENTRYPOINT
    }
}

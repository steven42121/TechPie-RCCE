package club.geekpie.techpie.background

import android.content.Context
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
import java.util.concurrent.TimeUnit

/**
 * Wakes the Dart side briefly to refresh the schedule island and to report new
 * card spending, then destroys the engine and returns. Nothing stays resident:
 * a killed app keeps no process, and the next scheduled run brings its own.
 */
class RefreshWorker(appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    override fun doWork(): Result {
        val engine = FlutterEngine(applicationContext)
        val messenger = engine.dartExecutor.binaryMessenger
        val finished = CountDownLatch(1)
        MethodChannel(messenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "done") {
                result.success(true)
                finished.countDown()
            } else {
                result.notImplemented()
            }
        }
        // The activity hosts these while the app is open; here the engine has
        // no activity, so the same bridges are registered against a context.
        IslandBridge(
            messenger,
            IslandCapabilityProbe(applicationContext),
            IslandNotificationFactory(
                applicationContext,
                FocusSequenceStore(applicationContext),
            ),
        )
        SpendingNotificationBridge(applicationContext, messenger)
        try {
            val loader = FlutterInjector.instance().flutterLoader()
            loader.startInitialization(applicationContext)
            loader.ensureInitializationComplete(applicationContext, null)
            engine.dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint(loader.findAppBundlePath(), ENTRYPOINT),
            )
            finished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        } finally {
            // The engine is the only thing holding a process here; releasing it
            // is what makes this a wake-up rather than a service.
            engine.destroy()
        }
        return Result.success()
    }

    companion object {
        const val CHANNEL = "techpie/background"
        const val ENTRYPOINT = "backgroundRefresh"
        private const val TIMEOUT_SECONDS = 60L
    }
}

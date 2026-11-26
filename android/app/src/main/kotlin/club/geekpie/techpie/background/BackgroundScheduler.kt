package club.geekpie.techpie.background

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

/**
 * The one periodic wake-up behind the island and the spending alerts. Fifteen
 * minutes is the floor for periodic work, and it is enough: the card system has
 * no push to be late for, and the app itself refreshes the moment it opens.
 */
object BackgroundScheduler {
    const val WORK_NAME = "techpie-refresh"
    private const val ISLAND_REBUILD_WORK_NAME = "techpie-island-timeline-rebuild"
    const val INTERVAL_MINUTES = 15L

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<RefreshWorker>(
            INTERVAL_MINUTES,
            TimeUnit.MINUTES,
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    fun rebuildIslandTimeline(context: Context) {
        val request = OneTimeWorkRequestBuilder<RefreshWorker>()
            .setInputData(workDataOf(RefreshWorker.ISLAND_ONLY_KEY to true))
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ISLAND_REBUILD_WORK_NAME,
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            request,
        )
    }
}

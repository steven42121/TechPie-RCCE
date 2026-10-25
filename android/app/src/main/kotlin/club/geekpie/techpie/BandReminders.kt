package club.geekpie.techpie

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.xiaomi.xms.wearable.Wearable

/** 上课提醒排程：AlarmManager 触发广播，即使 app 进程不在前台也能推送到手环。 */
object BandReminders {
    const val ACTION = "club.geekpie.techpie.BAND_REMINDER"
    const val EXTRA_TITLE = "title"
    const val EXTRA_BODY = "body"

    data class Item(val atMillis: Long, val title: String, val body: String)

    fun schedule(context: Context, items: List<Item>) {
        cancelAll(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val exact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
        items.forEachIndexed { index, item ->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intentFor(context, item.title, item.body),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            if (exact) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    item.atMillis,
                    pendingIntent,
                )
            } else {
                // 未授予精确闹钟权限时退化为非精确调度，仍可提醒但可能延迟。
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    item.atMillis,
                    pendingIntent,
                )
            }
        }
        Log.i("BandReminders", "scheduled ${items.size} reminders (exact=$exact)")
    }

    fun cancelAll(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (index in 0 until MAX_REMINDERS) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intentFor(context, "", ""),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
            ) ?: continue
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun intentFor(context: Context, title: String, body: String): Intent =
        Intent(context, BandReminderReceiver::class.java).apply {
            action = ACTION
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_BODY, body)
        }

    private const val MAX_REMINDERS = 64
}

class BandReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BandReminders.ACTION) return
        val title = intent.getStringExtra(BandReminders.EXTRA_TITLE) ?: "TechPie"
        val body = intent.getStringExtra(BandReminders.EXTRA_BODY) ?: ""
        val pendingResult = goAsync()
        Wearable.getNodeApi(context).getConnectedNodes()
            .addOnSuccessListener { nodes ->
                val node = nodes.firstOrNull()
                if (node == null) {
                    pendingResult.finish()
                    return@addOnSuccessListener
                }
                Wearable.getNotifyApi(context).sendNotify(node.id, title, body)
                    .addOnSuccessListener { pendingResult.finish() }
                    .addOnFailureListener { pendingResult.finish() }
            }
            .addOnFailureListener { pendingResult.finish() }
    }
}

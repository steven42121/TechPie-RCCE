package club.geekpie.techpie

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.xiaomi.xms.wearable.Wearable
import org.json.JSONArray
import org.json.JSONObject

/** 上课提醒排程：AlarmManager 触发广播，即使 app 进程不在前台也能推送到手环。 */
object BandReminders {
    const val ACTION = "club.geekpie.techpie.BAND_REMINDER"
    const val EXTRA_TITLE = "title"
    const val EXTRA_BODY = "body"

    private const val PREFS = "techpie_band_reminders"
    private const val ITEMS_KEY = "items"
    private const val NODE_ID_KEY = "band_node_id"
    private const val MAX_REMINDERS = 64

    data class Item(val atMillis: Long, val title: String, val body: String)

    /** 记住最近连上的手环节点：广播进程冷启动时不必重新等设备发现。 */
    fun rememberNode(context: Context, nodeId: String) {
        if (nodeId.isBlank()) return
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(NODE_ID_KEY, nodeId).apply()
    }

    fun knownNode(context: Context): String? =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(NODE_ID_KEY, null)?.takeIf { it.isNotBlank() }

    fun schedule(context: Context, items: List<Item>) {
        cancelAll(context)
        persist(context, items)
        register(context, items)
    }

    /** 开机、时间跳变、应用更新都会清掉 AlarmManager 闹钟；用持久化副本恢复未来的提醒。 */
    fun restore(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val raw = prefs.getString(ITEMS_KEY, null) ?: return
        val items = try {
            val now = System.currentTimeMillis()
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    val at = item.getLong("at")
                    if (at > now) {
                        add(Item(at, item.getString("title"), item.getString("body")))
                    }
                }
            }
        } catch (_: Exception) {
            emptyList()
        }
        if (items.isEmpty()) return
        register(context, items)
        Log.i("BandReminders", "restored ${items.size} reminders")
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
        // 取消提醒时同步清掉持久化副本，避免开机恢复已取消的提醒。
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(ITEMS_KEY).apply()
    }

    private fun register(context: Context, items: List<Item>) {
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
        Log.i("BandReminders", "registered ${items.size} reminders (exact=$exact)")
    }

    private fun persist(context: Context, items: List<Item>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("at", item.atMillis)
                    .put("title", item.title)
                    .put("body", item.body),
            )
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(ITEMS_KEY, array.toString()).apply()
    }

    private fun intentFor(context: Context, title: String, body: String): Intent =
        Intent(context, BandReminderReceiver::class.java).apply {
            action = ACTION
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_BODY, body)
        }
}

/**
 * 到点时系统可能为新进程触发广播：穿戴 SDK 的服务绑定是异步的，第一次
 * getConnectedNodes() 往往还是空列表，而旧实现只查一次就静默放弃，于是
 * 手机有通知、手环没动静。这里先试上次连过的节点，再短轮询发现，失败留日志。
 */
class BandReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != BandReminders.ACTION) return
        val title = intent.getStringExtra(BandReminders.EXTRA_TITLE) ?: "TechPie"
        val body = intent.getStringExtra(BandReminders.EXTRA_BODY) ?: ""
        // 手机端同时震动提醒；手环不在连接时用户也不会错过。
        notifyPhone(context, title, body)
        val appContext = context.applicationContext
        val pendingResult = goAsync()
        val done = {
            runCatching { pendingResult.finish() }
            Unit
        }
        val known = BandReminders.knownNode(appContext)
        if (known != null) {
            send(appContext, known, title, body) { delivered ->
                if (delivered) {
                    done()
                } else {
                    discover(appContext, ATTEMPTS) { nodeId ->
                        if (nodeId == null) {
                            Log.w(TAG, "band reminder not delivered: no reachable node")
                            done()
                        } else {
                            send(appContext, nodeId, title, body) { done() }
                        }
                    }
                }
            }
        } else {
            discover(appContext, ATTEMPTS) { nodeId ->
                if (nodeId == null) {
                    Log.w(TAG, "band reminder not delivered: no reachable node")
                    done()
                } else {
                    send(appContext, nodeId, title, body) { done() }
                }
            }
        }
    }

    /** 冷启动时 SDK 可能还没绑定好，轮询几次再判定"没有设备"。 */
    private fun discover(context: Context, attemptsLeft: Int, onDone: (String?) -> Unit) {
        Wearable.getNodeApi(context).getConnectedNodes()
            .addOnSuccessListener { nodes ->
                val id = nodes.firstOrNull()?.id
                if (id != null) {
                    BandReminders.rememberNode(context, id)
                    onDone(id)
                } else {
                    retryDiscovery(context, attemptsLeft, onDone)
                }
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "getConnectedNodes failed: ${error.message}")
                retryDiscovery(context, attemptsLeft, onDone)
            }
    }

    private fun retryDiscovery(context: Context, attemptsLeft: Int, onDone: (String?) -> Unit) {
        if (attemptsLeft <= 1) {
            onDone(null)
            return
        }
        Handler(Looper.getMainLooper()).postDelayed(
            { discover(context, attemptsLeft - 1, onDone) },
            DISCOVERY_INTERVAL_MS,
        )
    }

    private fun send(
        context: Context,
        nodeId: String,
        title: String,
        body: String,
        onDone: (Boolean) -> Unit,
    ) {
        Wearable.getNotifyApi(context).sendNotify(nodeId, title, body)
            .addOnSuccessListener {
                Log.i(TAG, "band reminder delivered to $nodeId")
                onDone(true)
            }
            .addOnFailureListener { error ->
                Log.w(TAG, "sendNotify to $nodeId failed: ${error.message}")
                onDone(false)
            }
    }

    private fun notifyPhone(context: Context, title: String, body: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return // Android 13+ 未授予通知权限时静默丢弃。
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "上课提醒", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "上课前在手机上震动提醒"
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 250, 500)
                },
            )
        }
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(NOTIFY_ID, notification)
    }

    private companion object {
        const val TAG = "BandReminders"
        const val CHANNEL_ID = "band_reminder"
        const val NOTIFY_ID = 48292

        /** 冷启动时等 SDK 绑定完成：5 次 × 1.2s，留在广播的 10s 预算内。 */
        const val ATTEMPTS = 5
        const val DISCOVERY_INTERVAL_MS = 1200L
    }
}

/** 重启/时间跳变/应用更新会把 AlarmManager 闹钟清掉，用持久化副本恢复。 */
class BandReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
            -> BandReminders.restore(context)
        }
    }
}

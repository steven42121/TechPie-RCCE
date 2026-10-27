package club.geekpie.techpie.island

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import club.geekpie.techpie.keepalive.KeepAlive
import club.geekpie.techpie.keepalive.KeepAliveService
import org.json.JSONArray
import org.json.JSONObject

/**
 * Keeps the island on screen while the app is not running.
 *
 * The Dart side precomputes the frames (it owns the schedule), this side stores
 * them and posts each one at its time through AlarmManager. Reboot, clock
 * changes and package updates restore the future ones, the way the band
 * reminders do. A null frame means "nothing to say any more": dismiss.
 */
object IslandAlarms {
    const val ACTION = "club.geekpie.techpie.ISLAND_FRAME"
    const val EXTRA_FRAME = "frame"

    private const val TAG = "IslandAlarms"
    private const val PREFS = "techpie_island_alarms"
    private const val ITEMS_KEY = "frames"
    private const val MAX_EVENTS = 256

    /** [frameJson] null asks the receiver to dismiss instead of posting. */
    data class Item(val atMillis: Long, val frameJson: String?)

    fun schedule(context: Context, items: List<Item>) {
        cancelAll(context)
        val capped = items
            .filter { it.atMillis > System.currentTimeMillis() }
            .sortedBy { it.atMillis }
            .take(MAX_EVENTS)
        persist(context, capped)
        register(context, capped)
        Log.i(TAG, "scheduled ${capped.size} island frames")
    }

    fun cancelAll(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (index in 0 until MAX_EVENTS) {
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                index,
                intentFor(context, null),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
            ) ?: continue
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().remove(ITEMS_KEY).apply()
    }

    /** Re-register what survived: AlarmManager forgets everything across a reboot. */
    fun restore(context: Context) {
        val now = System.currentTimeMillis()
        val items = load(context).filter { it.atMillis > now }
        if (items.isEmpty()) return
        register(context, items)
        Log.i(TAG, "restored ${items.size} island frames")
    }

    /**
     * Runs in the receiver, possibly in a process the alarm just woke: the focus
     * grant is a ContentProvider round trip, so a first probe can miss on a cold
     * start. Before degrading to a plain notification, probe once more.
     */
    fun deliver(context: Context, frameJson: String?, onDone: () -> Unit = {}) {
        if (frameJson == null) {
            dismiss(context)
            onDone()
            return
        }
        val frame = IslandFrame.fromJson(frameJson)
        if (frame == null) {
            Log.w(TAG, "unreadable frame; nothing to post")
            onDone()
            return
        }
        val probe = IslandCapabilityProbe(context)
        val snapshot = probe.snapshot()
        if (snapshot.backend == IslandBackend.PLAIN) {
            Handler(Looper.getMainLooper()).postDelayed({
                val backend = probe.refresh().backend
                publish(context, frame, frameJson, backend)
                onDone()
            }, RETRY_MS)
            return
        }
        publish(context, frame, frameJson, snapshot.backend)
        onDone()
    }

    private fun publish(
        context: Context,
        frame: IslandFrame,
        frameJson: String,
        backend: IslandBackend,
    ) {
        try {
            if (KeepAlive.isEnabled(context)) {
                KeepAliveService.start(context, frameJson, backend)
            } else {
                IslandNotificationFactory(context, FocusSequenceStore(context))
                    .updateFrame(frame, backend, IslandNotificationFactory.NOTIFICATION_ID)
            }
            Log.i(TAG, "posted island frame (${backend.name.lowercase()})")
        } catch (error: Exception) {
            Log.w(TAG, "posting the island frame failed: ${error.message}")
        }
    }

    private fun dismiss(context: Context) {
        if (KeepAlive.isEnabled(context)) {
            KeepAliveService.start(context)
        } else {
            IslandNotificationFactory(context, FocusSequenceStore(context))
                .cancel(IslandNotificationFactory.NOTIFICATION_ID)
        }
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
                intentFor(context, item.frameJson),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            if (exact) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    item.atMillis,
                    pendingIntent,
                )
            } else {
                // 没有精确闹钟权限时仍尽力送达：晚几分钟也比不出现强。
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    item.atMillis,
                    pendingIntent,
                )
            }
        }
    }

    private fun persist(context: Context, items: List<Item>) {
        val array = JSONArray()
        items.forEach { item ->
            array.put(
                JSONObject()
                    .put("at", item.atMillis)
                    .put("frame", item.frameJson?.let { JSONObject(it) } ?: JSONObject.NULL),
            )
        }
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(ITEMS_KEY, array.toString()).apply()
    }

    private fun load(context: Context): List<Item> {
        val raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(ITEMS_KEY, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            val items = mutableListOf<Item>()
            for (i in 0 until array.length()) {
                val entry = array.getJSONObject(i)
                val frame = if (entry.isNull("frame")) {
                    null
                } else {
                    entry.getJSONObject("frame").toString()
                }
                items.add(Item(entry.getLong("at"), frame))
            }
            items
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun intentFor(context: Context, frameJson: String?): Intent =
        Intent(context, IslandFrameReceiver::class.java).apply {
            action = ACTION
            if (frameJson != null) putExtra(EXTRA_FRAME, frameJson)
        }

    private const val RETRY_MS = 800L
}

/** One alarm per precomputed frame; a null extra is the "dismiss" event. */
class IslandFrameReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != IslandAlarms.ACTION) return
        val pending = goAsync()
        IslandAlarms.deliver(
            context.applicationContext,
            intent.getStringExtra(IslandAlarms.EXTRA_FRAME),
        ) { runCatching { pending.finish() } }
    }
}
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
import club.geekpie.techpie.background.BackgroundScheduler
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
    const val EXTRA_GENERATION = "generation"
    const val EXTRA_AT = "at"
    const val EXTRA_REPAINT = "repaint"

    internal data class Delivery(
        val frameJson: String?,
        val expiresAtMillis: Long?,
        val eventAtMillis: Long,
        val dismiss: Boolean,
    )

    private const val TAG = "IslandAlarms"
    private const val PREFS = "techpie_island_alarms"
    private const val ITEMS_KEY = "frames"
    private const val GENERATION_KEY = "generation"
    private const val CURRENT_FRAME_KEY = "current_frame"
    private const val CURRENT_EXPIRES_KEY = "current_expires_at"
    private const val CURRENT_AT_KEY = "current_at"
    private const val MAX_EVENTS = 256

    /** [frameJson] null asks the receiver to dismiss instead of posting. */
    data class Item(
        val atMillis: Long,
        val frameJson: String?,
        val expiresAtMillis: Long? = frameJson?.let { IslandFrame.fromJson(it)?.expiresAt },
    )

    @Synchronized
    fun schedule(context: Context, items: List<Item>): Long {
        cancelAll(context)
        cancelRepaint(context)
        val generation = currentGeneration(context)
        val capped = items
            .filter { it.atMillis > System.currentTimeMillis() }
            .sortedBy { it.atMillis }
            .take(MAX_EVENTS)
        persist(context, capped)
        register(context, capped, generation)
        Log.i(TAG, "scheduled ${capped.size} island frames")
        return generation
    }

    @Synchronized
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
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val state = readState(context)
        writeState(
            context,
            state.copy(
                generation = state.generation + 1L,
                currentFrameJson = null,
                currentFrameExpiresAt = null,
                currentFrameAtMillis = System.currentTimeMillis(),
            ),
        )
        prefs.edit().remove(ITEMS_KEY).apply()
    }

    fun currentGeneration(context: Context): Long = readState(context).generation

    @Synchronized
    fun recordCurrentFrame(
        context: Context,
        generation: Long,
        frameJson: String?,
        expiresAt: Long?,
        eventAtMillis: Long = System.currentTimeMillis(),
    ): Boolean {
        val state = readState(context)
        if (state.generation != generation || eventAtMillis < state.currentFrameAtMillis) {
            return false
        }
        val updated = state.copy(
            currentFrameJson = frameJson,
            currentFrameExpiresAt = expiresAt,
            currentFrameAtMillis = eventAtMillis,
        )
        writeState(context, updated)
        return true
    }

    private fun readState(context: Context): AlarmState {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return AlarmState(
            generation = prefs.getLong(GENERATION_KEY, 0L),
            currentFrameJson = prefs.getString(CURRENT_FRAME_KEY, null),
            currentFrameExpiresAt = if (prefs.contains(CURRENT_EXPIRES_KEY)) {
                prefs.getLong(CURRENT_EXPIRES_KEY, 0L)
            } else {
                null
            },
            currentFrameAtMillis = prefs.getLong(CURRENT_AT_KEY, 0L),
        )
    }

    private fun writeState(context: Context, state: AlarmState) {
        val editor = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putLong(GENERATION_KEY, state.generation)
            .putLong(CURRENT_AT_KEY, state.currentFrameAtMillis)
        if (state.currentFrameJson == null) {
            editor.remove(CURRENT_FRAME_KEY).remove(CURRENT_EXPIRES_KEY)
        } else {
            editor.putString(CURRENT_FRAME_KEY, state.currentFrameJson)
            state.currentFrameExpiresAt?.let { editor.putLong(CURRENT_EXPIRES_KEY, it) }
                ?: editor.remove(CURRENT_EXPIRES_KEY)
        }
        editor.apply()
    }

    internal fun resolveFrameForDelivery(
        currentGeneration: Long,
        currentFrameJson: String?,
        currentExpiresAt: Long?,
        currentFrameAtMillis: Long,
        generation: Long,
        scheduledAt: Long,
        incomingFrameJson: String?,
        isRepaint: Boolean,
        items: List<Item>,
        nowMillis: Long,
    ): Delivery? {
        if (generation != currentGeneration || scheduledAt > nowMillis) return null
        if (!isRepaint && items.none { it.atMillis == scheduledAt }) return null

        val latestDue = items.asSequence()
            .filter { it.atMillis <= nowMillis }
            .maxByOrNull { it.atMillis }
        val candidate = when {
            latestDue != null && latestDue.atMillis >= currentFrameAtMillis -> latestDue
            isRepaint && currentFrameJson == incomingFrameJson ->
                Item(currentFrameAtMillis, currentFrameJson, currentExpiresAt)
            else -> return null
        }
        val frameJson = candidate.frameJson
        val expired = candidate.expiresAtMillis?.let { it <= nowMillis } == true
        val dismiss = frameJson == null || expired
        return Delivery(
            frameJson = frameJson.takeUnless { dismiss },
            expiresAtMillis = candidate.expiresAtMillis,
            eventAtMillis = candidate.atMillis,
            dismiss = dismiss,
        )
    }

    private data class AlarmState(
        val generation: Long,
        val currentFrameJson: String?,
        val currentFrameExpiresAt: Long?,
        val currentFrameAtMillis: Long,
    )

    @Synchronized
    fun isCurrentFrame(context: Context, generation: Long, frameJson: String): Boolean {
        val state = readState(context)
        return state.generation == generation && state.currentFrameJson == frameJson
    }

    @Synchronized
    internal fun isCurrentFrame(
        context: Context,
        generation: Long,
        frameJson: String,
        eventAtMillis: Long,
    ): Boolean {
        val state = readState(context)
        return state.generation == generation &&
            state.currentFrameJson == frameJson &&
            state.currentFrameAtMillis == eventAtMillis
    }

    /** Invalidate queued alarms before rebuilding from Dart's current cached schedule. */
    fun invalidate(context: Context, dismissCurrent: Boolean) {
        cancelAll(context)
        cancelRepaint(context)
        if (dismissCurrent && !KeepAlive.isEnabled(context)) {
            IslandNotificationFactory(context, FocusSequenceStore(context))
                .cancel(IslandNotificationFactory.NOTIFICATION_ID)
        }
    }

    /**
     * HyperOS gives the floating focus slot to one notification at a time, so
     * another app's message takes the island away from a class that is still
     * running. Re-posting the same ongoing frame brings it back; this alarm
     * repeats that quietly until the frame's own expiry, then stops.
     */
    @Synchronized
    fun scheduleRepaint(
        context: Context,
        frameJson: String,
        generation: Long = currentGeneration(context),
    ) {
        if (!isCurrentFrame(context, generation, frameJson)) return
        val frame = IslandFrame.fromJson(frameJson)
        val next = nextRepaintAt(frame, System.currentTimeMillis(), REPAINT_INTERVAL_MS)
        if (next == null) {
            cancelRepaint(context, generation)
            return
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REPAINT_REQUEST_CODE,
            intentFor(context, frameJson, generation, next, true),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val exact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
        if (exact) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                next,
                pendingIntent,
            )
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next, pendingIntent)
        }
    }

    internal fun nextRepaintAt(frame: IslandFrame?, nowMillis: Long, intervalMillis: Long): Long? {
        if (frame?.kind != "class") return null
        val expiresAt = frame.expiresAt ?: return null
        val next = nowMillis + intervalMillis
        return next.takeIf { it < expiresAt }
    }

    @Synchronized
    fun cancelRepaint(context: Context, expectedGeneration: Long? = null) {
        if (expectedGeneration != null && currentGeneration(context) != expectedGeneration) return
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REPAINT_REQUEST_CODE,
            intentFor(context, null, currentGeneration(context), 0L, true),
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        ) ?: return
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /**
     * Runs in the receiver, possibly in a process the alarm just woke: the focus
     * grant is a ContentProvider round trip, so a first probe can miss on a cold
     * start. Before degrading to a plain notification, probe once more.
     */
    fun deliver(
        context: Context,
        frameJson: String?,
        generation: Long,
        scheduledAtMillis: Long,
        isRepaint: Boolean,
        onDone: () -> Unit = {},
    ) {
        processDelivery(
            context, frameJson, generation, scheduledAtMillis, isRepaint,
            refreshProbe = false, onDone = onDone,
        )
    }

    private fun processDelivery(
        context: Context,
        incomingFrameJson: String?,
        generation: Long,
        scheduledAtMillis: Long,
        isRepaint: Boolean,
        refreshProbe: Boolean,
        onDone: () -> Unit,
    ) {
        val now = System.currentTimeMillis()
        val state = readState(context)
        val resolved = resolveFrameForDelivery(
            currentGeneration = state.generation,
            currentFrameJson = state.currentFrameJson,
            currentExpiresAt = state.currentFrameExpiresAt,
            currentFrameAtMillis = state.currentFrameAtMillis,
            generation = generation,
            scheduledAt = scheduledAtMillis,
            incomingFrameJson = incomingFrameJson,
            isRepaint = isRepaint,
            items = load(context),
            nowMillis = now,
        )
        if (resolved == null) {
            onDone()
            return
        }
        if (resolved.dismiss) {
            if (!recordCurrentFrame(
                    context, generation, null, null, resolved.eventAtMillis,
                )
            ) {
                onDone()
                return
            }
            cancelRepaint(context, generation)
            dismiss(context)
            onDone()
            return
        }

        val frameJson = resolved.frameJson ?: run { onDone(); return }
        val frame = IslandFrame.fromJson(frameJson)
        if (frame == null) {
            if (!recordCurrentFrame(context, generation, null, null, resolved.eventAtMillis)) {
                onDone()
                return
            }
            cancelRepaint(context, generation)
            dismiss(context)
            onDone()
            return
        }
        if (!recordCurrentFrame(
                context, generation, frameJson, frame.expiresAt, resolved.eventAtMillis,
            )
        ) {
            onDone()
            return
        }
        val probe = IslandCapabilityProbe(context)
        val backend = if (refreshProbe) probe.refresh().backend else probe.snapshot().backend
        if (backend == IslandBackend.PLAIN && !refreshProbe) {
            Handler(Looper.getMainLooper()).postDelayed({
                processDelivery(
                    context, incomingFrameJson, generation, scheduledAtMillis, isRepaint,
                    refreshProbe = true, onDone = onDone,
                )
            }, RETRY_MS)
            return
        }
        if (!isCurrentFrame(context, generation, frameJson, resolved.eventAtMillis)) {
            onDone()
            return
        }
        publish(context, frame, frameJson, backend, generation, resolved.eventAtMillis)
        scheduleRepaint(context, frameJson, generation)
        onDone()
    }

    private fun publish(
        context: Context,
        frame: IslandFrame,
        frameJson: String,
        backend: IslandBackend,
        generation: Long,
        eventAtMillis: Long,
    ) {
        try {
            if (KeepAlive.isEnabled(context)) {
                KeepAliveService.start(
                    context, frameJson, backend, generation, eventAtMillis,
                )
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

    private fun register(context: Context, items: List<Item>, generation: Long) {
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
                intentFor(context, item.frameJson, generation, item.atMillis, false),
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

    private fun intentFor(
        context: Context,
        frameJson: String?,
        generation: Long = -1L,
        atMillis: Long = -1L,
        repaint: Boolean = false,
    ): Intent =
        Intent(context, IslandFrameReceiver::class.java).apply {
            action = ACTION
            if (frameJson != null) putExtra(EXTRA_FRAME, frameJson)
            putExtra(EXTRA_GENERATION, generation)
            putExtra(EXTRA_AT, atMillis)
            putExtra(EXTRA_REPAINT, repaint)
        }

    private const val RETRY_MS = 800L

    /** How often a running class re-asserts its slot after another app takes it. */
    private const val REPAINT_INTERVAL_MS = 5 * 60 * 1000L

    /** Outside the frame range (0 until MAX_EVENTS) so cancelAll never touches it. */
    private const val REPAINT_REQUEST_CODE = 10000
}

/** One alarm per precomputed frame; a null extra is the "dismiss" event. */
class IslandFrameReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != IslandAlarms.ACTION) return
        val pending = goAsync()
        IslandAlarms.deliver(
            context.applicationContext,
            intent.getStringExtra(IslandAlarms.EXTRA_FRAME),
            intent.getLongExtra(IslandAlarms.EXTRA_GENERATION, -1L),
            intent.getLongExtra(IslandAlarms.EXTRA_AT, -1L),
            intent.getBooleanExtra(IslandAlarms.EXTRA_REPAINT, false),
        ) { runCatching { pending.finish() } }
    }
}
/** Rebuild wall-clock alarms from Flutter's current cached schedule. */
class IslandRestoreReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (!shouldRebuildForAction(action)) return
        val appContext = context.applicationContext
        IslandAlarms.invalidate(appContext, dismissCurrent = true)
        BackgroundScheduler.rebuildIslandTimeline(appContext)
    }

    companion object {
        internal fun shouldRebuildForAction(action: String): Boolean = action in setOf(
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_TIME_CHANGED,
            Intent.ACTION_TIMEZONE_CHANGED,
            Intent.ACTION_DATE_CHANGED,
            Intent.ACTION_MY_PACKAGE_REPLACED,
        )
    }
}

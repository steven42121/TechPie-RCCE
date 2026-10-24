package club.geekpie.techpie.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import club.geekpie.techpie.MainActivity
import club.geekpie.techpie.R
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * Local receipts for campus-card spending.
 *
 * The card system offers no push, so this only ever reports what the app itself
 * read from the ledger; Dart decides which records are new and whether the user
 * wants to hear about them at all.
 */
class SpendingNotificationBridge(
    private val activity: Activity,
    messenger: BinaryMessenger,
) {
    private val channel = MethodChannel(messenger, CHANNEL)
    private val appContext = activity.applicationContext
    private val manager = appContext.getSystemService(NotificationManager::class.java)

    init {
        ensureChannel()
        channel.setMethodCallHandler { call, result ->
            when (call.method) {
                "post" -> post(call, result)
                "ensurePermission" -> result.success(requestNotificationPermission())
                else -> result.notImplemented()
            }
        }
    }

    fun dispose() {
        channel.setMethodCallHandler(null)
    }

    private fun post(call: MethodCall, result: MethodChannel.Result) {
        val title = call.argument<String>("title")
        val body = call.argument<String>("body")
        val key = call.argument<String>("key")
        if (title.isNullOrBlank() || body.isNullOrBlank() || key.isNullOrBlank()) {
            result.error("BAD_ARGS", "title, body and key are required", null)
            return
        }
        if (!NotificationManagerCompat.from(appContext).areNotificationsEnabled()) {
            // Denied at the system level: the ledger screen still has the numbers.
            result.success(false)
            return
        }
        val id = key.hashCode()
        val notification = NotificationCompat.Builder(appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_spending_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(contentIntent(id))
            .setGroup(GROUP_KEY)
            .setAutoCancel(true)
            .apply {
                if (call.argument<Boolean>("silent") == true) setSilent(true)
            }
            .build()
        manager.notify(id, notification)
        result.success(true)
    }

    /** A tap lands on the app, where the balance and the full receipt are. */
    private fun contentIntent(id: Int): PendingIntent {
        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            appContext,
            id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    private fun ensureChannel() {
        manager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                appContext.getString(R.string.spending_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = appContext.getString(R.string.spending_channel_desc)
                setShowBadge(false)
            },
        )
    }

    private fun requestNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val granted = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_NOTIFICATION_PERMISSION,
            )
        }
        return granted
    }

    private companion object {
        const val CHANNEL = "techpie/notification"
        const val CHANNEL_ID = "spending_alerts"
        const val GROUP_KEY = "club.geekpie.techpie.SPENDING"
        const val REQUEST_NOTIFICATION_PERMISSION = 48293
    }
}

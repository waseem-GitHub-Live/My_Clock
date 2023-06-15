package uz.murodjon_sattorov.myclock.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import uz.murodjon_sattorov.myclock.R
import uz.murodjon_sattorov.myclock.views.activities.MainActivity
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED) {
        } else {
            val dayOfWeek = intent.getIntExtra("dayOfWeek", -1)
            if (dayOfWeek != -1) {
                val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                if (currentDayOfWeek == dayOfWeek) {
                    showNotification(context)
                }
            }
        }
    }

    private fun showNotification(context: Context) {
        val channelId = "alarm_channel"
        val channelName = "Alarm Channel"
        val channelDescription = "Channel for alarm notifications"

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelDescription
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent for the notification
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_round_access_alarm_24)
            .setContentTitle("Alarm Notification")
            .setContentText("This is a notification for your alarm")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Show the notification
        val notificationId = 1
        notificationManager.notify(notificationId, builder.build())
    }
}
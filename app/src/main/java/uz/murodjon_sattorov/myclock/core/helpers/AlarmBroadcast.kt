package uz.murodjon_sattorov.myclock.core.helpers

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.antonious.materialdaypicker.MaterialDayPicker
import uz.murodjon_sattorov.myclock.R
import uz.murodjon_sattorov.myclock.receivers.AlarmReceiver
import java.util.*


class AlarmBroadcast {

    private var calendar: Calendar? = null
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    fun setAlarm(context: Context, hour: Int, minutes: Int, requestCode: Int) {
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager?.cancel(pendingIntent)

        calendar = Calendar.getInstance()
        calendar!!.set(Calendar.HOUR_OF_DAY, hour)
        calendar!!.set(Calendar.MINUTE, minutes)
        calendar!!.set(Calendar.SECOND, 0)
        calendar!!.set(Calendar.MILLISECOND, 0)

        val currentTime = Calendar.getInstance().timeInMillis
        val alarmTime = calendar!!.timeInMillis
        if (alarmTime <= currentTime) {
            calendar!!.add(Calendar.DAY_OF_YEAR, 1)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager?.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar!!.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                calendar!!.timeInMillis,
                pendingIntent
            )
        }
        Toast.makeText(context, "Alarm set successfully", Toast.LENGTH_SHORT).show()
    }



    fun cancelAlarm(context: Context, requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        if (alarmManager == null) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        alarmManager?.cancel(pendingIntent)
        Toast.makeText(context, "Alarm canceled", Toast.LENGTH_SHORT).show()
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "alarm_channel"
            val channelName = "Alarm Channel"
            val channelDescription = "Description of My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, title: String, content: String, selectedDays: List<Int>) {
        val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        if (!selectedDays.contains(currentDayOfWeek)) {
            return
        }

        val notificationBuilder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.ic_round_access_alarm_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
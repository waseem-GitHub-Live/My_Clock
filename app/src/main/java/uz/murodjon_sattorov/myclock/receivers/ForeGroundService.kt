package uz.murodjon_sattorov.myclock.receivers

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import uz.murodjon_sattorov.myclock.R
import uz.murodjon_sattorov.myclock.views.activities.MainActivity

class ForeGroundService : Service() {


    val NOTIFICATION_ID = 1
    val CHANNEL_ID = "ForegroundServiceChannel"


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val contentText = intent?.getStringExtra("contentText") ?: ""
        val notification = createNotification(contentText)

        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    private fun createNotification(contentText: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Notification Title")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_round_access_alarm_24)
            .setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
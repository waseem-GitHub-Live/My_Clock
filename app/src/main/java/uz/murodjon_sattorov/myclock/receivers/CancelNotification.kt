package uz.murodjon_sattorov.myclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat


class CancelNotification: BroadcastReceiver() {
    private var notificationManager: NotificationManagerCompat? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        notificationManager = NotificationManagerCompat.from(context!!)
        notificationManager!!.cancel(1)
    }
}
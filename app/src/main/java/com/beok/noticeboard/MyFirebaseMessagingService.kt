package com.beok.noticeboard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) = Unit

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isEmpty()) return

        sendNotification(remoteMessage)
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "fcm"
            notiManager.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    "wuhan",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(false)
                    vibrationPattern = longArrayOf(100, 200, 100, 200)
                })

            val notiBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setChannelId(channelId)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND + Notification.DEFAULT_VIBRATE)
            notiManager.notify(NOTI_FCM_ID, notiBuilder.build())
        } else {
            val notiBuilder = NotificationCompat.Builder(this, "")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND + Notification.DEFAULT_VIBRATE)
            notiManager.notify(NOTI_FCM_ID, notiBuilder.build())
        }
    }

    companion object {
        const val NOTI_FCM_ID = 856
    }
}
package com.wcs.mobilehris.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.wcs.mobilehris.R
import com.wcs.mobilehris.feature.dtlnotification.DetailNotificationActivity
import com.wcs.mobilehris.feature.preparation.splash.SplashActivity


@Suppress("DEPRECATION")
class HrisService : FirebaseMessagingService() {
    private val TAG = "FirebaseMessagingService"
    private val responseData : JsonObject? = null

    fun onNewToken(){
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "NEW TOKEN $refreshedToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("###", "FROM: " + remoteMessage.from)
        if (remoteMessage.data.isNotEmpty()) { Log.d("###", "Message Data: " + remoteMessage.data) }
        if (remoteMessage.notification != null) {
            Log.d("###", "Message body: " + remoteMessage.notification?.body)
            sendNotification(remoteMessage.notification?.body)
        }
    }

    private fun sendNotification(body: String?) {
        val notificationSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "my_channel_01"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "my_channel"
            val Description = "This is my channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = Description
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.enableVibration(true)
            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_logo_wcs)
                .setContentTitle("Wcs Hris")
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(notificationSound)

        val resultIntent = Intent(this, DetailNotificationActivity::class.java)
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(SplashActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent: PendingIntent? = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)
        notificationManager.notify(1, builder.build())
    }
}
package com.zeinab.weatherapp

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationAlertManager (base: Context?) : ContextWrapper(base) {

    val channel_id = "channelID"
    val channel_name = "channel"
    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lightColor = R.color.white
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(channel)
    }


    fun getManager(): NotificationManager {
        if (manager == null) {
            manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return manager as NotificationManager
    }

    fun getChannelNotification(title: String?, message: String?): NotificationCompat.Builder? {

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        return NotificationCompat.Builder(applicationContext, channel_id)
            .setContentTitle(title)
            .setContentText(message)

            .setSound(uri)
            .setSmallIcon(R.drawable.cloud)
    }
}
package com.example.ECommerceApp.common.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

object NotificationUtil {
    private const val CHANNEL_ID = "default_channel_id"
    private const val CHANNEL_NAME = "Default"
    private const val CHANNEL_DESCRIPTION = "Default Notification Channel"
    private const val NOTIFICATION_ID = 123

    fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_MAX) // Set priority to MAX
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
            enableVibration(true) // Enable vibration for the channel
            setVibrationPattern(longArrayOf(0, 1000, 500, 1000)) // Set vibration pattern for the channel
        }
        notificationManager.createNotificationChannel(channel)
    }

}

package com.example.clubbers.ui.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.clubbers.R
import com.example.clubbers.ui.MainActivity

class WelcomeNotificationService(
    private val context: Context
){

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotifiction(){
        val notification = NotificationCompat.Builder(context, WELCOME_CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Welcome")
            .build()
        Log.d("Notidio", "arrivato")
        notificationManager.notify(1, notification)
    }

    companion object{
        const val WELCOME_CHANNEL_ID = "welcome_channel"
    }

}
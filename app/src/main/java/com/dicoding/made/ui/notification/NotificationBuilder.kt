package com.dicoding.made.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.dicoding.made.R
import com.dicoding.made.data.local.model.Movie
import com.dicoding.made.ui.main.MainActivity
import com.dicoding.made.ui.main.detail.DetailMovieActivity
import com.dicoding.made.utils.Constant

class NotificationBuilder {

    companion object {
        fun createNotification(
            context: Context,
            title: String,
            content: String,
            notifId: Int,
            data: Movie? = null
        ){
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val style = NotificationCompat.BigTextStyle()
            style.setBigContentTitle(title)
            style.bigText(content)

            val builder = NotificationCompat.Builder(context, Constant.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_movie))
                .setContentTitle(title)
                .setContentText(content)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setStyle(style)

            createPendingIntent(context, notifId, data)?.let {
                builder.setContentIntent(it)
            }

            val channel = NotificationChannel(
                Constant.CHANNEL_ID,
                Constant.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(notifId,builder.build())
        }

        private fun createPendingIntent(context: Context,notifId: Int,data: Movie?): PendingIntent? {

            if (notifId == Constant.ID_DAILY_REMINDER) {
                val intent = Intent(context.applicationContext, MainActivity::class.java)
                return PendingIntent.getActivity(context.applicationContext,notifId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
            }
            else {
                val backIntent = Intent(context.applicationContext, MainActivity::class.java)
                backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                val intent = Intent(context.applicationContext, DetailMovieActivity::class.java)
                intent.putExtra("detail",data?.id)
                intent.putExtra("type","movie")
                return PendingIntent.getActivities(
                    context.applicationContext,
                    notifId,
                    arrayOf(backIntent, intent),
                    PendingIntent.FLAG_ONE_SHOT
                )
            }
        }
    }
}
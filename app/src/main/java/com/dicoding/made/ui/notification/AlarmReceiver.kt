package com.dicoding.made.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dicoding.made.R
import com.dicoding.made.data.remote.repository.MovieRepository
import com.dicoding.made.ui.notification.NotificationBuilder.Companion.createNotification
import com.dicoding.made.utils.AppPreferences
import com.dicoding.made.utils.Constant
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val context: Context by inject()
    private val movieRepository: MovieRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        when (val id = intent.getIntExtra(Constant.ALARM_ID,Constant.ID_DAILY_REMINDER)) {
            Constant.ID_DAILY_REMINDER -> {
                createNotification(
                    context,
                    context.getString(R.string.app_name),
                    context.getString(R.string.content_daily_reminder),
                    id
                )
            }
            Constant.ID_NEW_RELEASE -> {
                CoroutineScope(Dispatchers.Default).launch {
                    AppPreferences.init(context)
                    val movies = movieRepository.getRelease(AppPreferences.language)
                    movies.results.forEachIndexed { position, movie ->
                        val content = context.getString(R.string.content_new_release, movie.title ?: "")
                        createNotification(
                            context,
                            context.getString(R.string.title_new_release),
                            content,
                            id + position + 1,
                            movie
                        )
                    }
                }
            }
        }
    }

    fun createAlarm(id: Int, hour: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(Constant.ALARM_ID, id)

        val now = Calendar.getInstance()
        now.set(Calendar.HOUR_OF_DAY, hour)
        now.set(Calendar.MINUTE, 0)
        now.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, now.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    fun cancelAlarm(id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }
}

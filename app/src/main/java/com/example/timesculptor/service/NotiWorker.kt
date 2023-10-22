package com.example.timesculptor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.MainActivity
import com.example.timesculptor.R
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.Calendar



class NotiWorker@AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repo: Repo
): CoroutineWorker(context, workerParams) {


//    lateinit var database: TimeSculptorDataBase
//    private lateinit var appDao: AppDao
    private lateinit var notificationManager: NotificationManager


    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO){
            try {
                //create instance and write things into database
//            database = TimeSculptorDataBase.getInstance(context)
//            appDao = database.timeSculptorDao



                //get date to get data
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val yesterday = calendar.time
                var totalTime = 0L
                var message = ""

                // need solve
                val data = repo.getYesterday(yesterday)
                data.forEach {
                    if (it != null){
                        totalTime += it.mUsageTime
                    }
                }
                message = totalTime.toHoursMinutesSeconds()
                Log.i("testnotification","$data")

                Log.i("testnotification","notiworker do something")

                //notification
                notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val channel = NotificationChannel(
                    "usage_channel",
                    "Usage Channel",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)

                val pendingIntent : PendingIntent = PendingIntent.getActivity(context,0,
                    Intent(context,MainActivity::class.java),FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

                val notificationBuilder =
                    NotificationCompat.Builder(applicationContext, "usage_channel")
                        .setContentTitle("Time Spend On APPs")
                        .setContentText("You spend $message on your phone yesterday")
                        .setSmallIcon(R.drawable.pomodoro)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                val notification = notificationBuilder.build()
                notificationManager.notify(1, notification)


                 Result.success()
            } catch (e: Exception) {
                Log.i("testnotification","worker got exception $e")
                 Result.failure()
            }
        }
    }

}
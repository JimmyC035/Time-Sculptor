package com.example.timesculptor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.R
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import java.sql.Date
import java.util.Calendar



class NotiWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


    lateinit var database: TimeSculptorDataBase
    private lateinit var appDao: AppDao
    lateinit var notificationManager: NotificationManager


    override fun doWork(): Result {

        try {
            //create instance and write things into database
            database = TimeSculptorDataBase.getInstance(applicationContext)
            appDao = database.TimeSculptorDao



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
            val data = appDao.getYesterday(yesterday)
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

            val notificationBuilder =
                NotificationCompat.Builder(applicationContext, "usage_channel")
                    .setContentTitle("Time Spend On APPs")
                    .setContentText("You spend $message on your phone yesterday")
                    .setSmallIcon(R.drawable.pomodoro)

            val notification = notificationBuilder.build()
            notificationManager.notify(1, notification)


            return Result.success()
        } catch (e: Exception) {
            Log.i("testnotification","worker got exception $e")
            return Result.failure()
        }

    }

}
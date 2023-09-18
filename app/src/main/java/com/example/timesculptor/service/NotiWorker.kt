package com.example.timesculptor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.R
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
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
//        //get data
//        val dataManager = DataManager()
//        val dailyData = dataManager.getApps(applicationContext,0)
//        appDao.insert(dailyData)


            //get date to get data
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val yesterday = calendar.time
            var totalTime = 0L
            var message = ""

            val data = appDao.getYesterday(yesterday)
            data.forEach {
                totalTime += it.mUsageTime
            }
            message = totalTime.toHoursMinutesSeconds()


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
            return Result.failure()
        }

    }

}
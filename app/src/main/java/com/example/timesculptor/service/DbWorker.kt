package com.example.timesculptor.service

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import java.lang.Exception
import java.util.Calendar

class DbWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


    lateinit var database: TimeSculptorDataBase
    private lateinit var appDao: AppDao
    private lateinit var repo: Repo


    override fun doWork(): Result {

        return try {
            //create instance and write things into database
            database = TimeSculptorDataBase.getInstance(applicationContext)
            appDao = database.TimeSculptorDao
            repo = Repo(appDao)
            //get data

            var dailyData = repo.getApps(applicationContext, 1)
            dailyData = dailyData.map { item ->
                val calendar = Calendar.getInstance()
                calendar.time = item.date
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                item.copy(date = calendar.time)
            }
            appDao.insert(dailyData)

            Log.i("testDBworker","worker do somthing")

            Result.success()
        } catch (e: Exception) {
            Log.i("testDBworker","worker got exception $e")
            Result.failure()
        }
    }
}

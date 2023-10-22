package com.example.timesculptor.service

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import java.lang.Exception
import java.util.Calendar

class DbWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {


    lateinit var database: TimeSculptorDataBase
    private lateinit var appDao: AppDao
    private lateinit var repo: Repo


    override suspend fun doWork(): Result {

        return try {
            //create instance and write things into database
            database = TimeSculptorDataBase.getInstance(applicationContext)
            appDao = database.timeSculptorDao
            repo = Repo(appDao)
            //get data
            val latest  = repo.getLatest()

            var dailyData = repo.getAppsForDB(applicationContext, latest!!)

            dailyData = dailyData.map { item ->
                val calendar = Calendar.getInstance()
                calendar.time = item.date
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                item.copy(date = calendar.time)
            }

            repo.insert(dailyData)
            Log.i("testDBworker","worker do somthing")

            Result.success()
        } catch (e: Exception) {
            Log.i("testDBworker","worker got exception $e")
            Result.failure()
        }
    }
}

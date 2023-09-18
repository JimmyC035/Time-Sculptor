package com.example.timesculptor.service

import android.app.NotificationManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import java.lang.Exception

class DbWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


    lateinit var database: TimeSculptorDataBase
    private lateinit var appDao: AppDao


    override fun doWork(): Result {

        return try {
            //create instance and write things into database
            database = TimeSculptorDataBase.getInstance(applicationContext)
            appDao = database.TimeSculptorDao
            //get data
            val dataManager = DataManager()
            val dailyData = dataManager.getApps(applicationContext, 0)
            appDao.insert(dailyData)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

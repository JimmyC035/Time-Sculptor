package com.example.timesculptor.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DbService : Service() {

    //    lateinit var database: TimeSculptorDataBase
//    private lateinit var appDao: AppDao
//    private lateinit var repo: Repo
    @Inject
    lateinit var repo: Repo


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("DBservice", "called")

        GlobalScope.launch {
            val latest = repo.getLatest()
            val dailyData = repo.getAppsForDB(applicationContext, latest!!)
            repo.insert(dailyData)
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
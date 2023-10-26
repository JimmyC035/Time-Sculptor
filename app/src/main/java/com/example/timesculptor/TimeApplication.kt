package com.example.timesculptor

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.work.WorkManager
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltAndroidApp
class TimeApplication: Application() {

//    @Inject
//    lateinit var database: TimeSculptorDataBase
//
//    @Inject
//    lateinit var appDao: AppDao

    @Inject
    lateinit var repo: Repo

//    companion object {
//        var instance: TimeApplication by Delegates.notNull()
//    }

    override fun onCreate() {
        super.onCreate()
//        instance = this


        val pref = this.getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val hour = pref.getInt("notification_hour", 8)
        val min = pref.getInt("notification_minute", 30)

        repo.createAndEnqueueDBWorker(this)
        repo.createAndEnqueueWorker(this,hour,min)
        Log.i("work","workers call?")


    }
}
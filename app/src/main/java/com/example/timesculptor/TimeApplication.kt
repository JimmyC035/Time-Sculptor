package com.example.timesculptor

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.work.WorkManager
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.ServiceLocator
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates

@HiltAndroidApp
class TimeApplication: Application() {

    lateinit var database : TimeSculptorDataBase
    private lateinit var appDao: AppDao
    private val timeSculptorRepository: TimeSculptorRepository
        get() = ServiceLocator.provideTasksRepository(this)

    lateinit var repo : Repo

    companion object {
        var instance: TimeApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        database = TimeSculptorDataBase.getInstance(applicationContext)
        appDao = database.TimeSculptorDao
        repo = Repo(appDao)

        val pref = this.getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val hour = pref.getInt("notification_hour", 8)
        val min = pref.getInt("notification_minute", 30)

        repo.createAndEnqueueDBWorker(this)
        repo.createAndEnqueueWorker(this,hour,min)
        Log.i("work","workers call?")




    }
}
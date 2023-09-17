package com.example.timesculptor

import android.app.Application
import com.example.timesculptor.data.source.source.ServiceLocator
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.HiltAndroidApp
import kotlin.properties.Delegates

@HiltAndroidApp
class TimeApplication: Application() {
    val timeSculptorRepository: TimeSculptorRepository
        get() = ServiceLocator.provideTasksRepository(this)

    companion object {
        var instance: TimeApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
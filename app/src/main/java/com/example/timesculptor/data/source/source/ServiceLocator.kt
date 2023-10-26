package com.example.timesculptor.data.source.source

import android.content.Context
import androidx.annotation.VisibleForTesting

//object ServiceLocator {
//
//    @Volatile
//    var timeSculptorRepository: TimeSculptorRepository? = null
//        @VisibleForTesting set
//
//    fun provideTasksRepository(context: Context): TimeSculptorRepository {
//        synchronized(this) {
//            return timeSculptorRepository
//                ?: createTimeSculptorRepository(context)
//        }
//    }
//
//    private fun createTimeSculptorRepository(context: Context): TimeSculptorRepository {
//        return TimeSculptorDataSource(TimeSculptorDataBase.getInstance(context).TimeSculptorDao)
//    }
//
//}
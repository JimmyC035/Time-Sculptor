package com.example.timesculptor.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.SessionData
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Calendar
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    fun getTodayMockData(context: Context,packageName:String):List<SessionData?>{
       return timeSculptorRepository.getTargetAppTimeline(context,packageName,0)
    }

    suspend fun getNotificationCount(packageName: String):Int{
        val endTime = System.currentTimeMillis()

        // Calculate the beginning of today (midnight)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val beginTime = calendar.timeInMillis
        return timeSculptorRepository.getTodayNotificationsCount(beginTime,endTime,packageName)
    }



}
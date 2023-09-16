package com.example.timesculptor.homepage

import android.app.usage.EventStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.example.timesculptor.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val TimeSculptorRepository: TimeSculptorRepository
) : ViewModel() {





    fun printEventStatsInfo(eventStats: EventStats) {
        val format = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault())
        val count = eventStats.count
        val eventType = eventStats.eventType
        val beginningTime = format.format(Date(eventStats.firstTimeStamp))
        val endTime = format.format(Date(eventStats.lastTimeStamp))
        val lastEventTime = format.format(Date(eventStats.lastEventTime))
        val totalTime = eventStats.totalTime / 1000



        Log.i("bqt", "| $count | $eventType | $beginningTime | $endTime | $lastEventTime | $totalTime | ")
    }

    fun testEventStats(context: Context) {
        val endTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val beginTime = calendar.timeInMillis
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = manager.queryEventStats(UsageStatsManager.INTERVAL_DAILY, AppUtil.getThisWeek()[0],endTime)
        Log.i("btttt","${usageStatsList.size}")

        for (eventStats in usageStatsList) {
            printEventStatsInfo(eventStats)
            Log.i("bttttt","${usageStatsList.size}")
        }
    }




}
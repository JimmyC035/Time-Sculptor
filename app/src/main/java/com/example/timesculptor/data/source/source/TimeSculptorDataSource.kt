package com.example.timesculptor.data.source.source

import android.content.Context
import android.graphics.LinearGradient
import androidx.lifecycle.LiveData
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.SessionData
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.UsageStatsSummary
import java.util.Date


class TimeSculptorDataSource(private val dao: AppDao) : TimeSculptorRepository {
    override suspend fun updatOrInsert(listAppItem: List<AppItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun getYesterday(yesterday: Date): List<AppItem?> {
        TODO("Not yet implemented")
    }


    override suspend fun getNotificationForToday(today: java.util.Date): List<NotificationHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationForYesterday(
        startTime: Long,
        endTime: Long
    ): List<NotificationHistory> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsTillNow(startOfDay: Long, current: Long): List<AppItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalUsageForDateItem(startOfDay: Long, endOfDay: Long): List<AppItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getTopAppByDate(startOfDay: Long, endOfDay: Long): UsageStatsSummary {
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationCountForTimeRange(startTime: Long, endTime: Long): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long): Long {
        TODO("Not yet implemented")
    }


    override fun createAndEnqueueWorker(context: Context,hour:Int,min:Int) {
        TODO("Not yet implemented")
    }

    override fun createAndEnqueueDBWorker(context: Context) {
        TODO("Not yet implemented")
    }

    override fun cancelAllWork(context: Context) {
        TODO("Not yet implemented")
    }

    override fun getTargetAppTimeline(
        context: Context,
        target: String,
        offset: Int
    ): List<SessionData?> {
        TODO("Not yet implemented")
    }

    override fun getApps(context: Context, offset: Int): List<AppItem> {
        TODO("Not yet implemented")
    }

    override fun getAppsForDB(context: Context,latest:Long): List<AppItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getLatest(): Long {
        TODO("Not yet implemented")
    }






}
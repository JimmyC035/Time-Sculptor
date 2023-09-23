package com.example.timesculptor.data.source.source

import android.content.Context
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.SessionData
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




}
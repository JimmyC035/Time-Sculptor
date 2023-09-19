package com.example.timesculptor.data.source.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.sql.Date


class TimeSculptorDataSource(private val dao: AppDao) : TimeSculptorRepository {
    //    override suspend fun insert(notificationHistory: NotificationHistory) {
//        dao.insert(notificationHistory)
//    }
    override fun getYesterday(yesterday: Date): List<AppItem?> {
        TODO("Not yet implemented")
    }


}
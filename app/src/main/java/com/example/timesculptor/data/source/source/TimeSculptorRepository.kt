package com.example.timesculptor.data.source.source

import android.content.Context
import androidx.room.Query
import com.example.timesculptor.data.source.NotificationHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.sql.Date

interface TimeSculptorRepository {

    fun hello(){}

    suspend fun insert(notificationHistory: NotificationHistory){}

    suspend fun insert(listAppItem: List<AppItem>){}

    @Query("SELECT * FROM app_item WHERE `date` = :yesterday")
    fun getYesterday(yesterday: Date): List<AppItem>

    fun createAndEnqueueWorker(context: Context){}

    fun createAndEnqueueDBWorker(context: Context){}
}
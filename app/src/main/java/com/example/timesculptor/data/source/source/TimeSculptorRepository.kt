package com.example.timesculptor.data.source.source

import android.content.Context
import androidx.room.Query
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.SessionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

interface TimeSculptorRepository {

    //for DB
    suspend fun insert(notificationHistory: NotificationHistory){}

    suspend fun insert(listAppItem: List<AppItem>){}

    suspend fun updatOrInsert(listAppItem: List<AppItem>)

    @Query("SELECT * FROM app_item WHERE DATE(`date`) = :yesterday")
    suspend fun getYesterday(yesterday: Date): List<AppItem?>

    @Query("SELECT * FROM notification_history WHERE DATE(`date`) = DATE(:today)")
    suspend fun getNotificationForToday(today: Date): List<NotificationHistory>

    @Query("SELECT * FROM notification_history WHERE created_time >= :startTime AND created_time <= :endTime")
    suspend fun getNotificationForYesterday(startTime:Long, endTime:Long): List<NotificationHistory>


    //for work manager
    fun createAndEnqueueWorker(context: Context,hour:Int,min:Int)

    fun createAndEnqueueDBWorker(context: Context)

    fun cancelAllWork(context: Context)


    //data from UsageStateManager
    fun getTargetAppTimeline(context: Context, target: String, offset: Int): List<SessionData?>

    fun getApps(context: Context, offset: Int): List<AppItem>



}
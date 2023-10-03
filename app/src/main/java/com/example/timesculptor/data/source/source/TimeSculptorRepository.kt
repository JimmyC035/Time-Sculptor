package com.example.timesculptor.data.source.source

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Query
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.SessionData
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.UsageStatsSummary
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

    @Query("SELECT * FROM app_item WHERE event_time BETWEEN :startOfDay AND :current")
    suspend fun getItemsTillNow(startOfDay: Long, current: Long): List<AppItem>

    @Query("SELECT *  FROM app_item WHERE event_time BETWEEN :startOfDay AND :endOfDay")
    suspend fun getTotalUsageForDateItem(startOfDay: Long, endOfDay: Long): List<AppItem>

    @Query("""
    SELECT package_name as packageName, 
           SUM(usage_time) as totalUsageTime,
           SUM(count) as totalUsageCount
    FROM app_item 
    WHERE event_time BETWEEN :startOfDay AND :endOfDay
    GROUP BY package_name
    ORDER BY totalUsageTime DESC
    LIMIT 1
    """)
    suspend fun getTopAppByDate(startOfDay: Long, endOfDay: Long): UsageStatsSummary

    @Query("SELECT COUNT(*) FROM notification_history WHERE created_time >= :startTime AND created_time <= :endTime")
    suspend fun getNotificationCountForTimeRange(startTime: Long, endTime: Long): Int

    @Query("SELECT SUM(usage_time) AS total_time FROM app_item WHERE event_time BETWEEN :startOfDay AND :endOfDay")
    suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long): Long


    //for work manager
    fun createAndEnqueueWorker(context: Context,hour:Int,min:Int)

    fun createAndEnqueueDBWorker(context: Context)

    fun cancelAllWork(context: Context)


    //data from UsageStateManager
    fun getTargetAppTimeline(context: Context, target: String, offset: Int): List<SessionData?>

    fun getApps(context: Context, offset: Int): List<AppItem>

    fun getAppsForDB(context: Context,latest:Long): List<AppItem>

    @Query("SELECT MAX(event_time) FROM app_item")
    suspend fun getLatest(): Long?




}
package com.example.timesculptor.data.source.source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.timesculptor.data.source.AppUsageData
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.UsageStatsSummary
import java.util.Date


@Dao
interface AppDao {

    @Insert
    fun insert(listAppItem: List<AppItem>)

    @Insert
    fun insert(notificationHistory: NotificationHistory)

    @Upsert
    fun updateOrInsert(listAppItem: List<AppItem>)

    @Query("SELECT * FROM app_item WHERE DATE(`date`) = DATE(:yesterday)")
    fun getYesterday(yesterday: Date): List<AppItem?>

    @Query("SELECT SUM(usage_time) AS total_time FROM app_item WHERE event_time BETWEEN :startOfDay AND :endOfDay")
    fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long): Long

    @Query("SELECT *  FROM app_item WHERE event_time BETWEEN :startOfDay AND :endOfDay")
    fun getTotalUsageForDateItem(startOfDay: Long, endOfDay: Long): List<AppItem>

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

    @Query("SELECT COUNT(*) FROM notification_history WHERE created_time BETWEEN :startOfDay AND :endOfDay AND package_name = :packageName")
    fun getTodayNotificationsCount(startOfDay: Long, endOfDay: Long, packageName: String): Int


    @Query("SELECT * FROM notification_history WHERE DATE(`date`) = DATE(:today)")
    fun getNotificationForToday(today: Date): List<NotificationHistory>

    @Query("SELECT * FROM notification_history WHERE created_time >= :startTime AND created_time <= :endTime")
    fun getNotificationForYesterday(startTime:Long, endTime:Long): List<NotificationHistory>

    @Query("SELECT COUNT(*) FROM notification_history WHERE created_time >= :startTime AND created_time <= :endTime")
    fun getNotificationCountForTimeRange(startTime: Long, endTime: Long): Int

    @Query("SELECT MAX(event_time) FROM app_item")
    fun getLatest(): Long?

    @Query("SELECT * FROM app_item WHERE event_time BETWEEN :startOfDay AND :current")
    fun getItemsTillNow(startOfDay: Long, current: Long): List<AppItem>


    @Update
    fun update(AppUsageData: AppUsageData)
}


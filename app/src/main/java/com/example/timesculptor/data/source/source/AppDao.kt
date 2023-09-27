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

    @Query("SELECT COALESCE(SUM(usage_time), 0) AS total_time FROM app_item WHERE event_time BETWEEN :startOfDay AND :endOfDay")
    fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long): LiveData<TotalUsage>

    @Query("SELECT * FROM notification_history WHERE DATE(`date`) = DATE(:today)")
    fun getNotificationForToday(today: Date): List<NotificationHistory>

    @Query("SELECT * FROM notification_history WHERE created_time >= :startTime AND created_time <= :endTime")
    fun getNotificationForYesterday(startTime:Long, endTime:Long): List<NotificationHistory>

    @Query("SELECT MAX(event_time) FROM app_item")
    fun getLatest(): Long?

    @Query("SELECT * FROM app_item WHERE event_time BETWEEN :startOfDay AND :current")
    fun getItemsTillNow(startOfDay: Long, current: Long): List<AppItem>


    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param product: [Product]
     */
    @Update
    fun update(AppUsageData: AppUsageData)
}


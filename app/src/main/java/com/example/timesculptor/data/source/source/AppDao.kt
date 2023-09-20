package com.example.timesculptor.data.source.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.timesculptor.data.source.AppUsageData
import com.example.timesculptor.data.source.NotificationHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date


@Dao
interface AppDao {

    @Insert
    fun insert(listAppItem: List<AppItem>)

    @Insert
    fun insert(notificationHistory: NotificationHistory)

    @Query("SELECT * FROM app_item WHERE DATE(`date`) = :yesterday")
    fun getYesterday(yesterday: Date): List<AppItem?>

    @Query("SELECT * FROM notification_history WHERE DATE(`date`) = DATE(:today)")
    fun getNotificationForToday(today: Date): List<NotificationHistory>

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param product: [Product]
     */
    @Update
    fun update(AppUsageData: AppUsageData)
}


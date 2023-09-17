package com.example.timesculptor.data.source.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.example.timesculptor.data.source.AppUsageData
import com.example.timesculptor.data.source.NotificationHistory


@Dao
interface AppDao {

    @Insert
    fun insert(AppUsageData: AppUsageData)

    @Insert
    fun insert(notificationHistory: NotificationHistory)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param product: [Product]
     */
    @Update
    fun update(AppUsageData: AppUsageData)
}


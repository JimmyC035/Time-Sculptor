package com.example.timesculptor.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update


@Dao
interface TimeSculptorDao {

    @Insert
    fun insert(AppUsageData: AppUsageData)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param product: [Product]
     */
    @Update
    fun update(AppUsageData: AppUsageData)
}
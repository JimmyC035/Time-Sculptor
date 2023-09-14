package com.example.timesculptor.data.source


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(foreignKeys = [ForeignKey(
    entity = DailyUsageData::class,
    parentColumns = arrayOf("generatedId"),
    childColumns = arrayOf("app_id"),
    onDelete = ForeignKey.CASCADE
)],tableName = "app_usage_table")
@Parcelize
data class AppUsageData(
    @PrimaryKey(autoGenerate = true)
    val key:Long = 0L,

    @ColumnInfo(name = "app_id")
    val appId : String ="",

    @ColumnInfo(name = "usage_time")
    val usageTime : Long =0L,

    @ColumnInfo(name = "date")
    val date : Long = 0L,

    @ColumnInfo(name = "notification")
    val notification : Int = 0,


) : Parcelable
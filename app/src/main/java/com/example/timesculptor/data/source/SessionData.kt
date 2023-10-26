package com.example.timesculptor.data.source

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "session_table")
@Parcelize
data class SessionData(
    @PrimaryKey(autoGenerate = true)
    var key: Long = 0L,

    @ColumnInfo(name = "app_name")
    var appName: String = "",

    @ColumnInfo(name = "package_name")
    var packageName: String = "",

    @ColumnInfo(name = "duration")
    var duration: Long = 0L,

    @ColumnInfo(name = "event_time")
    var eventTime: Long = 0L,

    @ColumnInfo(name = "event_type")
    var eventType: Int = 0,

    @ColumnInfo(name = "count")
    var count: Int = 0,

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverters::class)
    var date: Date = Date(),

    ) : Parcelable
package com.example.timesculptor.data.source.source

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.timesculptor.data.source.DateConverters
import kotlinx.parcelize.Parcelize
import java.util.Date


@Entity(tableName = "app_item")
@Parcelize
data class AppItem (

    @ColumnInfo(name = "name")
    var mName: String = "",

    @ColumnInfo(name = "package_name")
    var mPackageName: String = "",

    @ColumnInfo(name = "event_time")
    var mEventTime: Long = 0,

    @ColumnInfo(name = "usage_time")
    var mUsageTime: Long = 0,

    @ColumnInfo(name = "event_type")
    var mEventType: Int = 0,

    @ColumnInfo(name = "count")
    var mCount: Int = 0,

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverters::class)
    var date : Date = Date(),

//    @ColumnInfo(name = "is_system")
//    private var mIsSystem: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    var key: Long = 0L,

) : Parcelable

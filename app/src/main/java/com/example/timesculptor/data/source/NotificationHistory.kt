package com.example.timesculptor.data.source

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import java.util.Date


@Entity(tableName = "notification_history")
@Parcelize
data class NotificationHistory(



    @ColumnInfo(name = "package_name")
    var packageName : String ="",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "created_time")
    var createdTime : Long = 0L,

    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,

    @ColumnInfo(name = "date")
    @TypeConverters(DateConverters::class)
    var date : Date = Date(),

) : Parcelable
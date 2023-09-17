package com.example.timesculptor.data.source

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


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
    val id : Long = 0L
) : Parcelable
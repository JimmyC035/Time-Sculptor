package com.example.timesculptor.data.source

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "app_table")
@Parcelize
data class PackageData(
    @PrimaryKey(autoGenerate = true)
    val generatedId:Long = 0L,

    @ColumnInfo(name = "package_name")
    val packageName : String ="",

    @ColumnInfo(name = "app_label")
    val appLabel : String ="",

    @ColumnInfo(name = "category")
    val category : String ="",


) : Parcelable


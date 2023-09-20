package com.example.timesculptor.data.source

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun stringToDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }
}
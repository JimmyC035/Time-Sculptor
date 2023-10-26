package com.example.timesculptor.data.source

import android.util.Log
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun dateToString(date: Date?): String? {
        try {
            return date?.let { dateFormat.format(it) }
        } catch (e : Exception){
            Log.i("noti", "dateString: $e")
            throw e
        }
    }

    @TypeConverter
    fun stringToDate(dateString: String?): Date? {

        try{
            return dateString?.let { dateFormat.parse(it) }
        } catch (e: Exception){
            Log.i("noti", "dateString: $dateString")
            throw e
        }

    }
}
package com.example.timesculptor.data.source.source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.timesculptor.data.source.AppUsageData
import com.example.timesculptor.data.source.DateConverters
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.PackageData
import com.example.timesculptor.data.source.SessionData


@Database(entities = [AppUsageData::class, PackageData::class, SessionData::class, NotificationHistory::class,AppItem::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class TimeSculptorDataBase: RoomDatabase() {

    abstract val timeSculptorDao: AppDao

    companion object {


        @Volatile
        private var INSTANCE: TimeSculptorDataBase? = null

        fun getInstance(context: Context): TimeSculptorDataBase {

            synchronized(this) {
                var instance = INSTANCE
                // If instance is `null` make a new database instance.
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TimeSculptorDataBase::class.java,
                        "time_db"
                    )

                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
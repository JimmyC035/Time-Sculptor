package com.example.timesculptor.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.room.Room
import com.example.timesculptor.MainActivity
import com.example.timesculptor.R
import com.example.timesculptor.TimeApplication
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.data.source.source.TimeSculptorRepository

import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesTimeRepository(repo: Repo): TimeSculptorRepository

}
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): TimeSculptorDataBase{
        return Room.databaseBuilder(
            context.applicationContext,
            TimeSculptorDataBase::class.java,
            "time_db"
        ).build()
    }

    @Provides
    fun provideAppDao(dataBase: TimeSculptorDataBase): AppDao = dataBase.timeSculptorDao


}

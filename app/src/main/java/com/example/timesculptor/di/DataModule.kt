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
import com.example.timesculptor.service.ACTION_SHOW_TIMER_FRAGMENT
import com.example.timesculptor.service.NOTIFICATION_CHANNEL_ID
import dagger.Binds
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
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesTimeRepository(repo: Repo): TimeSculptorRepository

}
@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

//    @Provides
//    fun provideAppDao(): AppDao {
//        return TimeSculptorDataBase.getInstance(TimeApplication.instance.applicationContext).TimeSculptorDao
//    }
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
    fun provideAppDao(dataBase: TimeSculptorDataBase): AppDao = dataBase.TimeSculptorDao

//    @Provides
//    fun provideRepoImpl(dataBase: TimeSculptorDataBase) = Repo(dataBase.TimeSculptorDao)
}

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule{
    @Provides
    @ServiceScoped
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ) = PendingIntent.getActivity(
        app,
        0,
        Intent(app, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TIMER_FRAGMENT
        },
        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    @Provides
    @ServiceScoped
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.pomodoro)
        .setContentTitle("pomodoro")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

}
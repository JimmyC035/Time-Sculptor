package com.example.timesculptor.di

import android.content.Context
import androidx.room.Room
import com.example.timesculptor.TimeApplication
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
}
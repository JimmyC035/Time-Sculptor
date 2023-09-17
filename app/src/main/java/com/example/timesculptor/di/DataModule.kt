package com.example.timesculptor.di

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


@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesTimeRepository(repo: Repo): TimeSculptorRepository

}
@Module
@InstallIn(ViewModelComponent::class)
object DaoModule {

    @Provides
    fun provideAppDao(): AppDao {
        return TimeSculptorDataBase.getInstance(TimeApplication.instance.applicationContext).TimeSculptorDao
    }
}
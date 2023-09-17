package com.example.timesculptor.data.source.source

import android.util.Log
import com.example.timesculptor.data.source.NotificationHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repo @Inject constructor(private var dao :AppDao) : TimeSculptorRepository {
    override fun hello() {
        super.hello()
        Log.i("jason","hello")
    }

    override suspend fun insert(notificationHistory: NotificationHistory) {
        withContext(Dispatchers.IO){
            dao.insert(notificationHistory)
        }
    }


}
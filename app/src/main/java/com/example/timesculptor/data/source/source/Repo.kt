package com.example.timesculptor.data.source.source

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.service.DbWorker
import com.example.timesculptor.service.NotiWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Repo @Inject constructor(private var dao: AppDao) : TimeSculptorRepository {
    override fun hello() {
        super.hello()
        Log.i("jason", "hello")
    }

    override suspend fun insert(notificationHistory: NotificationHistory) {
        withContext(Dispatchers.IO) {
            dao.insert(notificationHistory)
        }
    }

    override suspend fun insert(listAppItem: List<AppItem>) {
        withContext(Dispatchers.IO) {
            dao.insert(listAppItem)
        }
    }

    override fun getYesterday(yesterday: Date): List<AppItem> {

        return dao.getYesterday(yesterday)
    }

    override fun createAndEnqueueWorker(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val currentTimeMillis = calendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        val initialDelay = if (currentTime > currentTimeMillis) {
            currentTimeMillis + TimeUnit.DAYS.toMillis(1) - currentTime
        } else {
            currentTimeMillis - currentTime
        }

        val workRequest = PeriodicWorkRequest.Builder(
            NotiWorker::class.java,
            1,
            TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SendNotificationWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.i("work", "called")
    }

    override fun createAndEnqueueDBWorker(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 58)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val currentTimeMillis = calendar.timeInMillis
        val currentTime = System.currentTimeMillis()

        val initialDelay = if (currentTime > currentTimeMillis) {
            currentTimeMillis + TimeUnit.DAYS.toMillis(1) - currentTime
        } else {
            currentTimeMillis - currentTime
        }

        val workRequest = PeriodicWorkRequest.Builder(
            DbWorker::class.java,
            1,
            TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "SendNotificationWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.i("work", "called")
    }


}
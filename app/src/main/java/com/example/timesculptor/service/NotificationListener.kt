package com.example.timesculptor.service

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.timesculptor.TimeApplication
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.util.AppUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NotificationListener : NotificationListenerService() {

   lateinit var database : TimeSculptorDataBase
   private lateinit var appDao: AppDao

    override fun onCreate() {
        super.onCreate()
         database = TimeSculptorDataBase.getInstance(applicationContext)
         appDao = database.TimeSculptorDao
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val notificationTime = sbn.postTime

        val packageName = sbn.packageName

        val notificationHistory = NotificationHistory()

        notificationHistory.name = AppUtil.parsePackageName(TimeApplication.instance.applicationContext.packageManager,packageName)
        notificationHistory.createdTime = notificationTime
        notificationHistory.packageName = packageName

        GlobalScope.launch{
            withContext(Dispatchers.IO){
                appDao.insert(notificationHistory)
            }
        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)

    }
}


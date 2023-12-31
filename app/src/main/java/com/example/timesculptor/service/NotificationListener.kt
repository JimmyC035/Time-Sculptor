package com.example.timesculptor.service

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.timesculptor.TimeApplication
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.source.AppDao
import com.example.timesculptor.data.source.source.Repo
import com.example.timesculptor.data.source.source.TimeSculptorDataBase
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.example.timesculptor.util.AppUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var repo: Repo


    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val notificationTime = sbn.postTime
//        val test = sbn.notification.extras.getString("android.text")
//        Log.i("notitest","$test")

        val packageName = sbn.packageName

        val notificationHistory = NotificationHistory()

        notificationHistory.name = AppUtil.parsePackageName(this.packageManager, packageName)
        notificationHistory.createdTime = notificationTime
        notificationHistory.packageName = packageName
        if (!AppUtil.isSystemApp(
                packageManager,
                notificationHistory.packageName
            ) && notificationHistory.packageName != "com.example.timesculptor"
        ) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    repo.insert(notificationHistory)
                }
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
    }
}


package com.example.timesculptor.data.source

import android.app.AppOpsManager
import android.app.NotificationManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.util.AppConst
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.SortEnum
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DataManager {

    fun requestUsagePermission(context: Context?) {
        val intent = Intent(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intent)
    }

    fun hasUsagePermissionGranted(context: Context?): Boolean {
        val appOpsManager = context!!.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    fun isNotificationAccessGranted(context: Context): Boolean {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val enabledListenerPackages = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = context.packageName
        return enabledListenerPackages?.contains(packageName) == true
    }

    fun requestNotificationAccess(context: Context) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivityForResult(intent, REQUEST_NOTIFICATION_ACCESS)
        context.startActivity(intent)
    }

    fun getTargetAppTimeline(context: Context, target: String, offset: Int): List<SessionData?> {
        val items: MutableList<SessionData?> = ArrayList()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val range = AppUtil.getTimeRange(SortEnum.getSortEnum(offset))
        val events = manager.queryEvents(range[0], range[1])
        val event = UsageEvents.Event()
        val item = SessionData()
        item.packageName = target
        item.appName = AppUtil.parsePackageName(context.packageManager, target)
        var prevEndEvent: ClonedEvent? = null
        var start: Long = 0
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            val currentPackage = event.packageName
            val eventType = event.eventType
            val eventTime = event.timeStamp
            Log.d(
                "********",
                currentPackage + " " + target + " " + SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss",
                    Locale.getDefault()
                ).format(
                    Date(eventTime)
                ) + " " + eventType
            )
            if (currentPackage == target) {
                Log.d(
                    "forins",
                    currentPackage + " " + target + " " + SimpleDateFormat(
                        "yyyy/MM/dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(
                        Date(eventTime)
                    ) + " " + eventType
                )
                // record first time
                if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                    Log.d("*******", "start $start")
                    if (start == 0L) {
                        start = eventTime
                        item.eventTime = eventTime
                        item.eventType = eventType
                        item.duration = 0
                        items.add(item.copy())
                    }
                } else if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                    if (start > 0) {
                        prevEndEvent = ClonedEvent(event)
                        item.duration = prevEndEvent.timeStamp - start
                        if (item.duration <= 0) item.duration = 0
                        items.add(item.copy())
                        start = 0
                    }
                    Log.d("*******", "add end $start")
                }
            } else {
                // record last
//                if (prevEndEvent != null && start > 0) {
//                    item.eventTime = prevEndEvent.timeStamp
//                    item.eventType = prevEndEvent.eventType
//                    item.duration = prevEndEvent.timeStamp - start
//                    if (item.duration <= 0) item.duration = 0
//                    if (item.duration > AppConst.USAGE_TIME_MIX) item.count++
//                    items.add(item.copy())
//                    start = 0
//                    prevEndEvent = null
//                }
            }
        }
        var total = 0L

        items.forEach{total += it!!.duration}

        Log.i("forins","${total/1000}")
        Log.i("foritem", items.toString())
        return items
    }

     // another method to get all apps usage but weird bug
    fun getApps(context: Context, offset: Int): List<AppItem> {
        val items: MutableList<AppItem> = ArrayList()
        val newList: MutableList<AppItem> = ArrayList()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var prevPackage = ""
        val startPoints: MutableMap<String, Long> = HashMap()
        val endPoints: MutableMap<String, ClonedEvent> = HashMap()
        val range = AppUtil.getTimeRange(SortEnum.getSortEnum(offset))
        val events = manager.queryEvents(range[0], range[1])
        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            //get event time stamp
            events.getNextEvent(event)
            val eventType = event.eventType
            val eventTime = event.timeStamp
            val eventPackage = event.packageName
            //set up first point
            if (eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                var item = containItem(items, eventPackage)
                if (item == null) {
                    item = AppItem()
                    item.mPackageName = eventPackage
                    items.add(item)
                }
                //assign start time if there's no
                if (!startPoints.containsKey(eventPackage)) {
                    startPoints[eventPackage] = eventTime
                }
            }
            //record end time
            if (eventType == UsageEvents.Event.ACTIVITY_PAUSED) {
                if (startPoints.isNotEmpty() && startPoints.containsKey(eventPackage)) {
                    endPoints[eventPackage] = ClonedEvent(event)
                    if (prevPackage == "") prevPackage = eventPackage // check if first iteration
                    if (prevPackage != eventPackage) { // check if package changed
                        if (startPoints.containsKey(prevPackage) && endPoints.containsKey(prevPackage)) {
                            val lastEndEvent = endPoints[prevPackage]
                            val listItem = containItem(items, prevPackage)
                            if (listItem != null) {
                                listItem.mEventTime = lastEndEvent!!.timeStamp
                                var duration = lastEndEvent.timeStamp - startPoints[prevPackage]!!
                                if (duration <= 0) duration = 0
                                listItem.mUsageTime += duration
                                if (duration > AppConst.USAGE_TIME_MIX) {
                                    listItem.mCount++
                                }
                            }
                            startPoints.remove(prevPackage)
                            endPoints.remove(prevPackage)
                        }
                        prevPackage = eventPackage
                    }
                }
            }
            //duration and used time count

        }
        if (items.size > 0) {
            val packageManager = context.packageManager
            val appsToKeep = setOf("com.google.android.youtube","com.instagram.android")
//                    && ignoreItem(item,appsToKeep)

            for (item in items) {
                if (!AppUtil.openable(packageManager, item.mPackageName)) {
                    continue
                }
                if (AppUtil.isSystemApp(packageManager, item.mPackageName!!)) {
                    continue
                }
                if (!AppUtil.isInstalled(packageManager, item.mPackageName!!)) {
                    continue
                }
                item.mName = AppUtil.parsePackageName(packageManager, item.mPackageName!!)
                newList.add(item)
            }
            newList.sortByDescending { it.mUsageTime }
        }
        return newList
    }




    private fun containItem(items: List<AppItem>, packageName: String): AppItem? {
        for (item in items) {
            if (item.mPackageName == packageName) return item
        }
        return null
    }

    private fun ignoreItem(items: AppItem, packageNameSet: Set<String>): Boolean {
        for (ignoreItem in packageNameSet) {
            if (items.mPackageName == ignoreItem) return false
        }
        return true
    }

    internal class ClonedEvent(event: UsageEvents.Event) {
        var packageName: String
        var eventClass: String
        var timeStamp: Long
        var eventType: Int

        init {
            packageName = event.packageName
            eventClass = event.className
            timeStamp = event.timeStamp
            eventType = event.eventType
        }
    }

}

package com.example.timesculptor.data.source

import android.app.AppOpsManager
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.compose.ui.unit.TextUnit
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.util.AppConst
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.PreferenceManager
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
                    "*******",
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
                    }
                    Log.d("*******", "add end $start")
                }
            } else {
                // record last
                if (prevEndEvent != null && start > 0) {
                    item.eventTime = prevEndEvent.timeStamp
                    item.eventType = prevEndEvent.eventType
                    item.duration = prevEndEvent.timeStamp - start
                    if (item.duration <= 0) item.duration = 0
                    if (item.duration > AppConst.USAGE_TIME_MIX) item.count++
                    items.add(item.copy())
                    start = 0
                    prevEndEvent = null
                }
            }
        }
        return items
    }

    // another method to get all apps usage but weird bug
    fun getApps(context: Context, sort: Int, offset: Int): List<AppItem> {
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
                }
            }
            //duration and used time count
            if (TextUtils.isEmpty(prevPackage)) prevPackage =
                eventPackage // check if first iteration
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
        if (items.size > 0) {
//            val hideSystem: Boolean = PreferenceManager()
//                .getBoolean(PreferenceManager.PREF_SETTINGS_HIDE_SYSTEM_APPS)
//            val hideUninstall: Boolean = PreferenceManager()
//                .getBoolean(PreferenceManager.PREF_SETTINGS_HIDE_UNINSTALL_APPS)
            val packageManager = context.packageManager

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
            when (sort) {
                0 -> {
                    newList.sortByDescending { it.mUsageTime }
                }

                1 -> {
                    newList.sortWith { left, right -> (right.mEventTime - left.mEventTime).toInt() }
                }

                2 -> {
                    newList.sortWith { left, right -> right.mCount - left.mCount }
                }
            }
        }
        return newList
    }


    fun getAll(context: Context, offset: Int): List<HomeItem> {
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val range = AppUtil.getTimeRange(SortEnum.getSortEnum(offset))
        val usageStatsList =
            manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, range[0], range[1])
        val packageManager = context.packageManager

        val usageMap = mutableMapOf<String, Long>()

        val appsToKeep = setOf("com.google.android.youtube")

        for (usageStats in usageStatsList) {
            val packageName = AppUtil.parsePackageName(packageManager, usageStats.packageName)
            val totalTimeInForeground = usageStats.totalTimeInForeground / 1000

            if (!AppUtil.isSystemApp(packageManager, packageName)) {
                val currentTotalTime = usageMap[packageName] ?: 0
                usageMap[packageName] = currentTotalTime + totalTimeInForeground
            }
        }

        val sortedItems = usageMap.entries.sortedByDescending { it.value }

        val topFiveItems = sortedItems.take(5)

        val homeItemList = mutableListOf<HomeItem>()
        for ((packageName, totalTimeInForeground) in topFiveItems) {
            val homeItem = HomeItem(packageName, totalTimeInForeground, 0)
            homeItemList.add(homeItem)
        }

        return homeItemList
    }





//    fun getAll(context: Context, offset: Int): List<HomeItem> {
//        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
//        val range = AppUtil.getTimeRange(SortEnum.getSortEnum(offset))
//        val usageStatsList =
//            manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, range[0], range[1])
//        val packageManager = context.packageManager
//
//        val usageMap = mutableMapOf<String, Long>()
//
//        for (usageStats in usageStatsList) {
//            val packageName = AppUtil.parsePackageName(packageManager, usageStats.packageName)
//            val totalTimeInForeground = usageStats.totalTimeInForeground / 1000
//            val currentTotalTime = usageMap[packageName] ?: 0
//            usageMap[packageName] = currentTotalTime + totalTimeInForeground
//        }
//
//        val homeItemList = mutableListOf<HomeItem>()
//        for ((packageName, totalTimeInForeground) in usageMap) {
//
//            val homeItem = HomeItem(packageName, totalTimeInForeground, 0)
//            homeItemList.add(homeItem)
//        }
//
//        return homeItemList
//    }


    private fun containItem(items: List<AppItem>, packageName: String): AppItem? {
        for (item in items) {
            if (item.mPackageName == packageName) return item
        }
        return null
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

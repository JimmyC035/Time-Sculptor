package com.example.timesculptor.data.source.source

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.data.source.SessionData
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.UsageStatsSummary
import com.example.timesculptor.service.DbWorker
import com.example.timesculptor.service.NotiWorker
import com.example.timesculptor.util.AppConst
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.SortEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Repo @Inject constructor(private var dao: AppDao) : TimeSculptorRepository {


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

    override suspend fun updatOrInsert(listAppItem: List<AppItem>) {
        withContext(Dispatchers.IO) {
            dao.updateOrInsert(listAppItem)
        }
    }

    override suspend fun getLatest(): Long? {
        return withContext(Dispatchers.IO) {
            if(dao.getLatest() == null){
                var currentTimeMillis = 0L
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                currentTimeMillis = calendar.timeInMillis
                currentTimeMillis

            }else{
                dao.getLatest()!! + 1000L
            }
        }
    }

    override suspend fun getTodayNotificationsCount(
        startOfDay: Long,
        endOfDay: Long,
        packageName: String
    ): Int {
        return withContext(Dispatchers.IO) {
            dao.getTodayNotificationsCount(startOfDay, endOfDay,packageName)
        }
    }

    override suspend fun getItemsTillNow(startOfDay: Long, current: Long): List<AppItem> {
        return withContext(Dispatchers.IO) {

            val items = dao.getItemsTillNow(startOfDay, current)
            items
        }
    }

    override suspend fun getTotalUsageForDateItem(startOfDay: Long, endOfDay: Long): List<AppItem> {
        return withContext(Dispatchers.IO) {

            val items = dao.getTotalUsageForDateItem(startOfDay,endOfDay)
            items
        }
    }

    override suspend fun getTopAppByDate(startOfDay: Long, endOfDay: Long): UsageStatsSummary {
        return withContext(Dispatchers.IO) {

            val items = dao.getTopAppByDate(startOfDay,endOfDay)
            items
        }
    }

    override suspend fun getNotificationCountForTimeRange(startTime: Long, endTime: Long): Int {
        return withContext(Dispatchers.IO) {
            dao.getNotificationCountForTimeRange(startTime, endTime)
        }
    }

    override suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long): Long {
        return withContext(Dispatchers.IO) {
            dao.getTotalUsageForDate(startOfDay, endOfDay)
        }
    }


    override suspend fun getYesterday(yesterday: Date): List<AppItem?> {

        return withContext(Dispatchers.IO) {
            dao.getYesterday(yesterday)
        }
    }

    override suspend fun getNotificationForToday(today: Date): List<NotificationHistory> {
        return withContext(Dispatchers.IO) {
            dao.getNotificationForToday(today)
        }
    }

    override suspend fun getNotificationForYesterday(
        startTime: Long,
        endTime: Long
    ): List<NotificationHistory> {
        return withContext(Dispatchers.IO) {
            dao.getNotificationForYesterday(startTime, endTime)

        }
    }

    override fun createAndEnqueueWorker(context: Context, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, min)
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
            24,
            TimeUnit.HOURS,
            15,
            TimeUnit.MINUTES
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            15,
            TimeUnit.SECONDS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag("notiWorker")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "NotificationWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.i("work", "$hour $min called notification")
    }

    override fun createAndEnqueueDBWorker(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 5)
        calendar.set(Calendar.MINUTE, 20)
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
            24,
            TimeUnit.HOURS,
            15,
            TimeUnit.MINUTES
        ).setBackoffCriteria(
            backoffPolicy = BackoffPolicy.LINEAR,
            15,
            TimeUnit.SECONDS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WriteDBWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.i("work", "called DB")
    }

    override fun cancelAllWork(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
        Log.i("work", "job canceled")
    }

    override fun getTargetAppTimeline(
        context: Context,
        target: String,
        offset: Int
    ): List<SessionData?> {
        val items: MutableList<SessionData?> = ArrayList()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val range = AppUtil.getTimeRange(SortEnum.getSortEnum(offset))
        val events = manager.queryEvents(range[0], range[1])
        val event = UsageEvents.Event()
        val item = SessionData()
        item.packageName = target
        item.appName = AppUtil.parsePackageName(context.packageManager, target)
        var prevEndEvent: DataManager.ClonedEvent? = null
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
                        prevEndEvent = DataManager.ClonedEvent(event)
                        item.duration = prevEndEvent.timeStamp - start
                        if (item.duration <= 0) item.duration = 0
                        item.eventType = eventType
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

        items.forEach { total += it!!.duration }

        Log.i("forins", "${total / 1000}")
        Log.i("foritem", items.toString())
        return items
    }

    override fun getApps(context: Context, offset: Int): List<AppItem> {
        val items: MutableList<AppItem> = ArrayList()
        val newList: MutableList<AppItem> = ArrayList()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var prevPackage = ""
        val startPoints: MutableMap<String, Long> = HashMap()
        val endPoints: MutableMap<String, DataManager.ClonedEvent> = HashMap()
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
                    endPoints[eventPackage] = DataManager.ClonedEvent(event)
                    if (prevPackage == "") prevPackage = eventPackage // check if first iteration
                    if (prevPackage != eventPackage) { // check if package changed
                        if (startPoints.containsKey(prevPackage) && endPoints.containsKey(
                                prevPackage
                            )
                        ) {
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
        //change to ignoreItem and exclude camera and settings
        if (items.size > 0) {
            val packageManager = context.packageManager
            val appsToKeep = setOf("com.example.timesculptor")

            for (item in items) {
                if (!AppUtil.openable(packageManager, item.mPackageName) || ignoreItem(
                        item,
                        appsToKeep
                    )
                ) {
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

    override fun getAppsForDB(context: Context,latest:Long): List<AppItem> {
        val items: MutableList<AppItem> = ArrayList()
        val newList: MutableList<AppItem> = ArrayList()
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var prevPackage = ""
        val startPoints: MutableMap<String, Long> = HashMap()
        val endPoints: MutableMap<String, DataManager.ClonedEvent> = HashMap()
        val range = AppUtil.getTillNow(latest)
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
                    endPoints[eventPackage] = DataManager.ClonedEvent(event)
                    if (prevPackage == "") prevPackage = eventPackage // check if first iteration
                    if (prevPackage != eventPackage) { // check if package changed
                        if (startPoints.containsKey(prevPackage) && endPoints.containsKey(
                                prevPackage
                            )
                        ) {
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
        //change to ignoreItem and exclude camera and settings
        if (items.size > 0) {
            val packageManager = context.packageManager
            val appsToKeep = setOf("com.example.timesculptor")

            for (item in items) {
                if (!AppUtil.openable(packageManager, item.mPackageName) || ignoreItem(
                        item,
                        appsToKeep
                    )
                ) {
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
            if (items.mPackageName == ignoreItem) return true
        }
        return false
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
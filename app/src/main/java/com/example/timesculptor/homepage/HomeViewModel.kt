package com.example.timesculptor.homepage

import android.app.usage.EventStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.example.timesculptor.util.AppUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val _totalTime = MutableStateFlow<Long>(0L)
    val totalTime: StateFlow<Long> = _totalTime

    private val _todayDate = MutableStateFlow<String>("")
    val todayDate :StateFlow<String> = _todayDate

    private val _notiCount = MutableLiveData<Int>()
    val notiCount :LiveData<Int> = _notiCount

    fun getNotification(today:Date){
//        viewModelScope.launch {
//            val notificationHistory= timeSculptorRepository.getNotificationForToday(today)
//            _notiCount.value = notificationHistory.size
//        }
    }

    fun getTodayDate() {
        val today = LocalDate.now()
        _todayDate.value = formatDate(today)
    }
    private fun formatDate(date: LocalDate): String {
        val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            .uppercase(Locale.ROOT)
        val month = date.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH).uppercase(Locale.ROOT)
        val dayOfMonth = date.dayOfMonth

        return "$dayOfWeek $month $dayOfMonth"
    }
    fun getUsage(context: Context)= viewModelScope.launch {
        var calculateTime: Long = 0L
        getHomePageData(context).forEach { calculateTime += it.mUsageTime }
        _totalTime.value = calculateTime
    }

    fun getHomePageData(context: Context):List<AppItem>{
       return timeSculptorRepository.getApps(context,0)
    }

    fun getAppIcon(context: Context,packageName:String): Drawable? {
        try {
            val packageManager = context.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            return packageManager.getApplicationIcon(appInfo)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    suspend fun updateDb(context: Context){
       val latest =  timeSculptorRepository.getLatest()
        val items = timeSculptorRepository.getAppsForDB(context,latest!!)
        timeSculptorRepository.insert(items)
        Log.i("update",latest.toString())
    }


    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val today = calendar.time
        getTodayDate()
        getNotification(today)
    }


    fun printEventStatsInfo(eventStats: EventStats) {
        val format = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault())
        val count = eventStats.count
        val eventType = eventStats.eventType
        val beginningTime = format.format(Date(eventStats.firstTimeStamp))
        val endTime = format.format(Date(eventStats.lastTimeStamp))
        val lastEventTime = format.format(Date(eventStats.lastEventTime))
        val totalTime = eventStats.totalTime / 1000



        Log.i("bqt", "| $count | $eventType | $beginningTime | $endTime | $lastEventTime | $totalTime | ")
    }

    fun testEventStats(context: Context) {
        val endTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val beginTime = calendar.timeInMillis
        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = manager.queryEventStats(UsageStatsManager.INTERVAL_DAILY, AppUtil.getThisWeek()[0],endTime)
        Log.i("btttt","${usageStatsList.size}")

        for (eventStats in usageStatsList) {
            printEventStatsInfo(eventStats)
            Log.i("bttttt","${usageStatsList.size}")
        }
    }




}
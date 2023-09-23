package com.example.timesculptor.today

import android.app.usage.EventStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val _totalTime = MutableStateFlow<Long>(0L)
    val totalTime: StateFlow<Long> = _totalTime

    private val _todayDate = MutableStateFlow<String>("")
    val todayDate : StateFlow<String> = _todayDate

    private val _notiCount = MutableLiveData<Int>()
    val notiCount : LiveData<Int> = _notiCount

    private val _notiYesterdayCount = MutableLiveData<Int>()
    val notiYesterdayCount : LiveData<Int> = _notiYesterdayCount

    private val _pickUpCount = MutableLiveData<Int>()
    val pickUpCount : LiveData<Int> = _pickUpCount

    private val _pickUpCountYesterday = MutableLiveData<Int>()
    val pickUpCountYesterday : LiveData<Int> = _pickUpCountYesterday

    private suspend fun getNotification(today: Date){
        val notificationHistory= timeSculptorRepository.getNotificationForToday(today)
        withContext(Dispatchers.Main) {
            _notiCount.value = notificationHistory.size
        }
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

    fun getMostUsedApp(context: Context):AppItem{
        return timeSculptorRepository.getApps(context,0)[0]
    }

    fun getHomePageData(context: Context):List<AppItem>{
        return timeSculptorRepository.getApps(context,0)
    }

    private suspend fun getYesterdayNotificationCount(){

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endTime = calendar.timeInMillis
        val count = timeSculptorRepository.getNotificationForYesterday(startTime,endTime).size
        _notiYesterdayCount.postValue(count)
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


    fun printEventStatsInfo(eventStats: EventStats) {
        val format = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault())
        _pickUpCount.value = eventStats.count
        val eventType = eventStats.eventType
        val beginningTime = format.format(Date(eventStats.firstTimeStamp))
        val endTime = format.format(Date(eventStats.lastTimeStamp))
        val lastEventTime = format.format(Date(eventStats.lastEventTime))
        val totalTime = eventStats.totalTime / 1000

        Log.i("bqt", "| ${pickUpCount.value} | $eventType | $beginningTime | $endTime | $lastEventTime | $totalTime | ")
    }


    fun testEventStatsForToday(context: Context) {
        val endTime = System.currentTimeMillis()

        // Calculate the beginning of today (midnight)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val beginTime = calendar.timeInMillis

        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = manager.queryEventStats(UsageStatsManager.INTERVAL_BEST,beginTime, endTime)


        for (eventStats in usageStatsList) {
            if (eventStats.eventType == 18 && eventStats.firstTimeStamp >= beginTime && eventStats.lastTimeStamp <= endTime) {
                printEventStatsInfo(eventStats)
            }
        }
    }

    fun testEventStatsForYesterday(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val startTime = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endTime = calendar.timeInMillis

        val manager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStatsList = manager.queryEventStats(UsageStatsManager.INTERVAL_BEST,startTime, endTime)


        for (eventStats in usageStatsList) {
            if (eventStats.eventType == 18 && eventStats.firstTimeStamp >= startTime ) {
                _pickUpCountYesterday.value = eventStats.count
                val format = SimpleDateFormat("yyyy.MM.dd_HH:mm:ss", Locale.getDefault())
                val eventType = eventStats.eventType
                val beginningTime = format.format(Date(eventStats.firstTimeStamp))
                val endTime = format.format(Date(eventStats.lastTimeStamp))
                val lastEventTime = format.format(Date(eventStats.lastEventTime))
                val totalTime = eventStats.totalTime / 1000


                Log.i("bqt", _pickUpCountYesterday.value.toString())
                Log.i("bqtYesterday", "| ${_pickUpCountYesterday.value} | $eventType | $beginningTime | $endTime | $lastEventTime | $totalTime | ")

            }
        }
    }


    init {
        getTodayDate()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val today = calendar.time
        viewModelScope.launch(Dispatchers.IO) {
            getNotification(today)
            getYesterdayNotificationCount()
        }
    }

}
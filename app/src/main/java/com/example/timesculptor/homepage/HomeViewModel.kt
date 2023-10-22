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
import com.example.timesculptor.data.source.PieChartDataset
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.example.timesculptor.util.AppUtil
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
class HomeViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val _totalTime = MutableStateFlow<Long>(0L)
    val totalTime: StateFlow<Long> = _totalTime

    private val _todayDate = MutableStateFlow<String>("")
    val todayDate :StateFlow<String> = _todayDate

    private val _pieChartDataset = MutableLiveData<PieChartDataset>()
    val pieChartDataset: LiveData<PieChartDataset> = _pieChartDataset


    private fun getTodayDate() {
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
    private fun getUsage(ListData:List<AppItem>)= viewModelScope.launch(Dispatchers.Default) {
        var calculateTime: Long = 0L
        ListData.forEach { calculateTime += it.mUsageTime }
        _totalTime.value = calculateTime
    }

    fun getHomePageData(context: Context){
        val pieChartData = timeSculptorRepository.getApps(context, 0)
        getUsage(pieChartData)
        val dataCount = if (pieChartData.size >= 7) 7 else pieChartData.size
        val dataItems = pieChartData.take(dataCount)
        val dataset = PieChartDataset()

        dataItems.forEach { item ->
            dataset.dataMap[item.mName] = item.mUsageTime
            getAppIcon(context, item.mPackageName)?.let { drawable ->
                dataset.icons.add(drawable)
            }
            dataset.packageNames.add(item.mPackageName)
        }

        _pieChartDataset.value = dataset
    }

    private fun getAppIcon(context: Context, packageName:String): Drawable? {
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
        withContext(Dispatchers.IO){
            val latest =  timeSculptorRepository.getLatest()
            val items = timeSculptorRepository.getAppsForDB(context,latest!!)
            timeSculptorRepository.insert(items)
            Log.i("update",latest.toString())
        }
    }



    init {
        getTodayDate()

    }



}
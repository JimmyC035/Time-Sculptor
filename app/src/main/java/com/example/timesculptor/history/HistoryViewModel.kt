package com.example.timesculptor.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.UsageStatsSummary
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val _totalUsageSelectedDate = MutableLiveData<Long>()
    val totalUsageSelectedDate: LiveData<Long> = _totalUsageSelectedDate

    private val _topUsed = MutableLiveData<UsageStatsSummary>()
    val topUsed: LiveData<UsageStatsSummary> = _topUsed

    private val _notificationCount = MutableLiveData<Int>()
    val notificationCount: LiveData<Int> = _notificationCount


    var monthAndDate = ""

    private suspend fun getNotificationCount(startOfDay: Long, endOfDay: Long){
        _notificationCount.postValue(
            timeSculptorRepository.getNotificationCountForTimeRange(
                startOfDay,
                endOfDay
            )
        )
    }

    private suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long) {
        _totalUsageSelectedDate.postValue(
            timeSculptorRepository.getTotalUsageForDate(
                startOfDay,
                endOfDay
            )
        )
        Log.i("total usage viewModel", totalUsageSelectedDate.toString())
    }

    private suspend fun getTopUsed(startOfDay: Long, endOfDay: Long){
        _topUsed.postValue(
            timeSculptorRepository.getTopAppByDate(
                startOfDay,
                endOfDay
            )
        )
    }

    fun loadDataForDate(startOfDay: Long, endOfDay: Long) {
        viewModelScope.launch {
            getNotificationCount(startOfDay,endOfDay)
            getTotalUsageForDate(startOfDay, endOfDay)
            getTopUsed(startOfDay, endOfDay)

        }
    }




}
package com.example.timesculptor.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.TotalUsage
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val  _totalUsageSelectedDate = MutableLiveData<Long>()
    val totalUsageSelectedDate :LiveData<Long> = _totalUsageSelectedDate
    suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long) {
        val totalUsage = timeSculptorRepository.getTotalUsageForDate(startOfDay, endOfDay).value?.total_time
        _totalUsageSelectedDate.postValue(totalUsage)
        Log.i("total usage", _totalUsageSelectedDate.value.toString())
    }



}
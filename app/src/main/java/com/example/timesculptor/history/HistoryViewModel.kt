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

    private val _totalUsageSelectedDate = MutableLiveData<Long>()
    val totalUsageSelectedDate: LiveData<Long> = _totalUsageSelectedDate

    //    val test: LiveData<Long> = timeSculptorRepository.getTotalUsageForDate(1695657600000,1695743999999)
    suspend fun getTotalUsageForDate(startOfDay: Long, endOfDay: Long) {
        _totalUsageSelectedDate.postValue(
            timeSculptorRepository.getTotalUsageForDate(
                startOfDay,
                endOfDay
            )
        )
        Log.i("total usage viewModel", totalUsageSelectedDate.toString())
    }



}
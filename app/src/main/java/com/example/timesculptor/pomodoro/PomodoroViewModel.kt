package com.example.timesculptor.pomodoro

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.example.timesculptor.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    private val _currentTime = MutableStateFlow(0L)
    val currentTime: StateFlow<Long> = _currentTime
    var isPause = false



    fun startTimer(context: Context) {
        val intent = Intent(context, TimerService::class.java)
        intent.also {
            it.putExtra("TIME_LEFT_IN_MILLIS", _currentTime.value)
            if(isPause){
                it.action = "RESUME"
            }else{
                it.action = "START_TIMER"
                isPause = false
            }
        }
        context.startService(intent)
    }

    fun cancelTimer(context: Context) {
        val intent = Intent(context, TimerService::class.java)
        intent.also {
            it.putExtra("TIME_LEFT_IN_MILLIS", 10000L)
            it.action = "CANCEL_TIMER"
        }
        context.startService(intent)
    }

    fun pauseTimer(context: Context) {
        val intent = Intent(context, TimerService::class.java)
        intent.also {
            it.putExtra("TIME_LEFT_IN_MILLIS", _currentTime.value)
            it.action = "PAUSE_TIMER"
        }
        isPause = true
        context.startService(intent)
    }
    fun addTime(){
        _currentTime.value += 60000L
        isPause = false
    }

    fun minusTime(){
        _currentTime.value -= 60000L
        isPause = false
    }
    fun resetTimer(){
        _currentTime.value = 10000L
    }

    private fun collectFlow(){
        viewModelScope.launch {
            TimerService.timeLeftFlow.collect{
                _currentTime.value = it
            }
        }
    }

    init {
        collectFlow()
    }
}


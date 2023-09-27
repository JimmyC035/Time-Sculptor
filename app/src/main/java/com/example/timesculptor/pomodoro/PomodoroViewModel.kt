package com.example.timesculptor.pomodoro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


    private val _timerState = MutableStateFlow(TimerViewState())
    val timerState: StateFlow<TimerViewState> = _timerState.asStateFlow()

    var currentTime:Long = 60000L

    fun onDecreaseClicked() {
        if (!timerState.value.isRunning) {
            val newTime = timerState.value.currentTime - 300000 // 减少300秒（300000毫秒）
            val updatedTime = if (newTime < 0) 0 else newTime // 确保时间不会小于0
            val newState = timerState.value.copy(currentTime = updatedTime)
            _timerState.value = newState
        }
    }

    fun onIncreaseClicked() {
        if (!timerState.value.isRunning) {
            val newTime = timerState.value.currentTime + 300000 // 增加300秒（300000毫秒）
            val newState = timerState.value.copy(currentTime = newTime)
            _timerState.value = newState
        }
    }
    fun onStartStopClicked() {
        val newIsRunning = !timerState.value.isRunning
        val newState = timerState.value.copy(isRunning = newIsRunning)
        _timerState.value = newState
    }
    fun decreaseTotalTime() {
        if (timerState.value.totalTime > 0 && timerState.value.isRunning) {
            val newTotalTime = timerState.value.totalTime - 100L // 减少100毫秒
            _timerState.value = timerState.value.copy(totalTime = newTotalTime)
        }
    }

}

data class TimerViewState(
    val totalTime: Long = 60000L,
    val currentTime: Long = 0L,
    val isRunning: Boolean = false
)
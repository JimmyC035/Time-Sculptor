package com.example.timesculptor.pomodoro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {



    var currentTime = 60000L


}
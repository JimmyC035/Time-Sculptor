package com.example.timesculptor.pomodoro

import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val TimeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


}
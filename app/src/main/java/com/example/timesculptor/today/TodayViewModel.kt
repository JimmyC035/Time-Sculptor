package com.example.timesculptor.today

import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val TimeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


}
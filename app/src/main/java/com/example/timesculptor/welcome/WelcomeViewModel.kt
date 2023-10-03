package com.example.timesculptor.welcome

import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WelcomeViewModel@Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


}
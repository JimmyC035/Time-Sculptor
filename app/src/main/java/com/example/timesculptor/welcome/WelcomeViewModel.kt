package com.example.timesculptor.welcome

import android.view.animation.Animation
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WelcomeViewModel@Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {



}
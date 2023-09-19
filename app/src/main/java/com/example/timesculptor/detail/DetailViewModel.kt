package com.example.timesculptor.detail

import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val TimeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


}
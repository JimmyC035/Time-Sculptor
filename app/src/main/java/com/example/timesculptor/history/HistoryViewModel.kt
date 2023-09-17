package com.example.timesculptor.history

import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val TimeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


}
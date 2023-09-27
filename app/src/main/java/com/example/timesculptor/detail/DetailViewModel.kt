package com.example.timesculptor.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.SessionData
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    fun getTodayMockData(context: Context,packageName:String):List<SessionData?>{
       return timeSculptorRepository.getTargetAppTimeline(context,packageName,0)
    }



}
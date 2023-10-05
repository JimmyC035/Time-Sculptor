package com.example.timesculptor.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UserSettingViewModel@Inject constructor(
    private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {

    fun updateWorker(context: Context, hour:Int, min:Int){
        timeSculptorRepository.createAndEnqueueWorker(context,hour,min)
        Log.i("work in setting","called")
    }




}
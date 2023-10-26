package com.example.timesculptor.welcome

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


@HiltViewModel
class Intro3ViewModel@Inject constructor(
private val timeSculptorRepository: TimeSculptorRepository
) : ViewModel() {


    private val _usagePermission = MutableStateFlow<Boolean>(false)
    val usagePermission : StateFlow<Boolean> = _usagePermission

    private val _overlayPermission = MutableStateFlow<Boolean>(false)
    val overlayPermission : StateFlow<Boolean> = _overlayPermission

    private val _notificationPermission = MutableStateFlow<Boolean>(false)
    val notificationPermission : StateFlow<Boolean> = _notificationPermission

    val combinedFlow: Flow<Boolean> = combine(
        _usagePermission,
        _overlayPermission,
        _notificationPermission
    ) { s1, s2, s3 ->
        s1 && s2 && s3
    }



    fun checkAndUpdateUsagePermissionState(context: Context,dataManager:DataManager) {
        val hasPermission = dataManager.hasUsagePermissionGranted(context)
        _usagePermission.value = hasPermission
    }

    fun checkAndUpdateOverlayPermissionState(context: Context) {
        val hasPermission = Settings.canDrawOverlays(context)
        Log.i("userPermission","overlay $hasPermission")
        _overlayPermission.value = hasPermission
    }

    fun checkAndUpdateNotificationPermissionState(context: Context,dataManager:DataManager) {
        val hasPermission = dataManager.isNotificationAccessGranted(context)
        Log.i("userPermission","notification $hasPermission")
        _notificationPermission.value = hasPermission
    }



}
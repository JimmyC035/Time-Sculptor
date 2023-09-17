package com.example.timesculptor.data.source.source

import com.example.timesculptor.data.source.NotificationHistory

interface TimeSculptorRepository {

    fun hello(){}

    suspend fun insert(notificationHistory: NotificationHistory){}
}
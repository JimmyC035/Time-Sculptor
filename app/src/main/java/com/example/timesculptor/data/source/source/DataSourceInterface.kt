package com.example.timesculptor.data.source.source

import com.example.timesculptor.data.source.NotificationHistory

interface DataSourceInterface {

    fun hello(){}

    suspend fun insert(notificationHistory: NotificationHistory){}
}
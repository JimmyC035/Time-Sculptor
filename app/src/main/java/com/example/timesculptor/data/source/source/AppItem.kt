package com.example.timesculptor.data.source.source

import java.util.Date

data class AppItem (
    var mName: String = "",
    var mPackageName: String = "",
    var mEventTime: Long = 0,
    var mUsageTime: Long = 0,
    var mEventType: Int = 0,
    var mCount: Int = 0,
    var date : Date = Date(),
    private var mIsSystem: Boolean = false
)

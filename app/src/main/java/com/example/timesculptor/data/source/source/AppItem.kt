package com.example.timesculptor.data.source.source

data class AppItem (
    var mName: String? = null,
    var mPackageName: String? = null,
    var mEventTime: Long = 0,
    var mUsageTime: Long = 0,
    var mEventType: Int = 0,
    var mCount: Int = 0,
    private var mIsSystem: Boolean = false
)

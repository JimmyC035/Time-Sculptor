package com.example.timesculptor.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log

class RebootService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Reboot service","called")
        val userPresentReceiver = UnlockReceiver()
        val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
        registerReceiver(userPresentReceiver, filter)


        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}
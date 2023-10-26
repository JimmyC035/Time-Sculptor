package com.example.timesculptor.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log

class RebootService : Service() {

    private val userPresentReceiver = UnlockReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("Reboot service","called")
        val filter = IntentFilter(Intent.ACTION_USER_PRESENT)
        registerReceiver(userPresentReceiver, filter)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(userPresentReceiver)
    }

}
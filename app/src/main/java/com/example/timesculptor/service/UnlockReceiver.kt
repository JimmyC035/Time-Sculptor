package com.example.timesculptor.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class UnlockReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("DBservice","onReceived22")
        if (intent?.action == Intent.ACTION_USER_PRESENT) {
            val serviceIntent = Intent(context, DbService::class.java)
            context?.startService(serviceIntent)
        }
    }
}
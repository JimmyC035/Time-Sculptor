package com.example.timesculptor.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RebootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Reboot service","onReceived reboot")
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, RebootService::class.java)
            context?.startService(serviceIntent)
        }
    }

}

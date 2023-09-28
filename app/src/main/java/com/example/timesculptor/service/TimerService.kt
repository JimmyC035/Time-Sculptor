package com.example.timesculptor.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.timesculptor.R
import kotlinx.coroutines.flow.MutableStateFlow

class TimerService : Service() {
    companion object {
        val timeLeftFlow = MutableStateFlow(60000L)
        val totalTime = MutableStateFlow(60000L)
    }
    var timerCount:CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            "START_TIMER" -> startTimer(intent)
            "PAUSE_TIMER" -> pauseTimer(intent)
        }

        return START_NOT_STICKY
    }

    private fun startTimer(intent: Intent?){
        timeLeftFlow.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 60000L) ?: 60000L
        totalTime.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 60000L) ?: 60000L
        startForegroundNotification()
        startCountdownTimer()
    }

    fun pauseTimer(intent: Intent?){
//        timeLeftFlow.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 0) ?: 0
//        totalTime.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 0) ?: 0
        stopService()
        pause()
    }

    private fun startForegroundNotification() {
        val notificationChannelId = "TIMER_CHANNEL"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Timer Notification Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Timer Running")
            .setContentText("Time left: ${timeLeftFlow.value}")
            .setSmallIcon(R.drawable.pomodoro)
            .build()

        startForeground(1, notification)
    }


    private fun startCountdownTimer() {
        timerCount = object : CountDownTimer(timeLeftFlow.value, 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftFlow.value = millisUntilFinished
                startForegroundNotification()

            }

            override fun onFinish() {
                stopService()

            }
        }.start()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun stopService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

// Stop the service from running in the foreground
        stopForeground(STOP_FOREGROUND_REMOVE)

// Remove the notification
        notificationManager.cancel(1)

        stopSelf()
    }

    fun pause(){
        timerCount?.cancel()
    }
}

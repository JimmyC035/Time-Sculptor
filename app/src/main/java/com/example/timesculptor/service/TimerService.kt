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
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.timesculptor.R
import com.example.timesculptor.util.AppUtil.toMinutesSeconds
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private  const val DEFAULT_TIME = 10000L

class TimerService : Service() {
    companion object {
        val timeLeftFlow = MutableStateFlow(DEFAULT_TIME)
        val totalTime = MutableStateFlow(DEFAULT_TIME)
        val isTimerRunningFlow = MutableStateFlow(false)
    }
    private var timerCount:CountDownTimer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            "START_TIMER" -> startTimer(intent)
            "PAUSE_TIMER" -> pauseTimer()
            "RESUME" -> resumeTimer(intent)
            "CANCEL_TIMER" -> cancelTimer()
        }

        return START_NOT_STICKY
    }

    private fun startTimer(intent: Intent?){
        timeLeftFlow.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 60000L) ?: 60000L
        totalTime.value = 0L
        totalTime.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 60000L) ?: 60000L
        isTimerRunningFlow.value = true
        startForegroundNotification()
        startCountdownTimer()
    }

    private fun resumeTimer(intent: Intent?){
        timeLeftFlow.value = intent?.getLongExtra("TIME_LEFT_IN_MILLIS", 60000L) ?: 60000L
        startForegroundNotification()
        isTimerRunningFlow.value = true
        startCountdownTimer()
    }

    private fun pauseTimer(){
        isTimerRunningFlow.value = false
        stopService()
        pause()
    }
    private fun cancelTimer(){
        timeLeftFlow.value = DEFAULT_TIME //set to default
        totalTime.value = 0L
        totalTime.value = DEFAULT_TIME
        isTimerRunningFlow.value = false
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
            .setContentText("Time left: ${timeLeftFlow.value.toMinutesSeconds()}")
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
               val notificationManager =
                    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val channel = NotificationChannel(
                    "Time_up",
                    "Time's Up",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channel)

                val notificationBuilder =
                    NotificationCompat.Builder(applicationContext, "Time_up")
                        .setContentTitle("Time's up")
                        .setContentText("Time's up take a five minutes break")
                        .setSmallIcon(R.drawable.pomodoro)

                val notification = notificationBuilder.build()
                notificationManager.notify(8, notification)
                GlobalScope.launch {
                    delay(500L)
                    timeLeftFlow.value = 10000L
                    totalTime.value = 10000L
                    stopService()
                }
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

    private fun pause(){
        timerCount?.cancel()
    }
}

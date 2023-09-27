package com.example.timesculptor.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.timesculptor.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val NOTIFICATION_CHANNEL_ID = "timer_channel"
const val NOTIFICATION_CHANNEL_NAME = "Timer"
const val NOTIFICATION_ID = 1 //at least 1, 0 does not work
const val ACTION_SHOW_TIMER_FRAGMENT = "showTimerFragment"

const val ACTION_START_SERVICE = "ACTION_START_SERVICE"

@AndroidEntryPoint
class TimerService : LifecycleService() {

    val binder = LocalBinder()

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder

    lateinit var curNotificationBuilder: NotificationCompat.Builder

    init {
        Log.i("timer", "Timer service is init")
    }
    companion object{
        val timeSelected = MutableLiveData(0)
        val timeProgress = MutableLiveData(0)
    }

    val isStart = MutableLiveData(true)
    var timeCountDown: CountDownTimer? = null
    private var pauseOffSet: Long = 0
    private var isFirst = true

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        if (timeCountDown != null) {
            timeCountDown?.cancel()
            timeProgress.postValue(0)
        }
        // ... rest of the onUnbind logic
        return super.onUnbind(intent)
    }
    override fun onCreate(){
        super.onCreate()
        curNotificationBuilder = baseNotificationBuilder

        isStart.observe(this, Observer {
            updateNotificationTrackingState(it)
        })
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            Log.i("timer", "${it?.action}")
            when (intent?.action) {
                ACTION_START_SERVICE -> {
                    if (isFirst) {
                        // startForegroundService()
                        isFirst = false
                    }
                    else{
                        startTimerSetup()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startTimer(selectedTime: Int, pauseOffSetL: Long) {
        startForegroundService()
        Log.i("timer", "currentSelectedTime: $selectedTime")
        timeCountDown = object : CountDownTimer(
            (selectedTime!! * 1000).toLong() - pauseOffSetL * 1000, 1000
        ) {
            override fun onTick(p0: Long) {
                timeProgress.postValue(timeProgress.value?.plus(1))
                Log.i("timer", "timeSelected: ${timeSelected.value}")
                Log.i("timer", "${timeSelected.value?.minus(timeProgress.value!!)}")
                pauseOffSet = timeSelected.value!!.toLong() - p0 / 1000

                val notification = curNotificationBuilder.setContentText(
                    timeSelected.value?.minus(timeProgress.value!!).toString())
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(NOTIFICATION_ID, notification.build())
            }

            override fun onFinish() {

                resetTime()
                // notification
            }
        }.start()
    }

    fun resetTime() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
            timeSelected.postValue(0)
            timeProgress.postValue(0)
            pauseOffSet = 0
            timeCountDown = null
            isStart.postValue(true)
            stopService()
        }
    }

    fun timePause() {
        if (timeCountDown != null) {
            timeCountDown!!.cancel()
        }
    }

    fun startTimerSetup() {
        if (timeSelected.value!! > timeProgress.value!!) {
            if (isStart.value!!) {
                startTimer(timeSelected.value!!,pauseOffSet)
                isStart.postValue(false)
            } else {
                // pause
                isStart.postValue(true)
                timePause()
            }
        }
    }

    fun addExtraTime() {
        if (timeSelected.value != 0) {
            timeSelected.postValue(timeSelected.value?.plus(15))
            timePause()
            startTimer(timeSelected.value!! + 15,pauseOffSet)
        }
    }

    private fun startForegroundService() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

//    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
//        this,
//        0,
//        Intent(this, MainActivity::class.java).also {
//            it.action = ACTION_SHOW_WORKOUT_FRAGMENT
//        },
//        FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("RestrictedApi")
    private fun updateNotificationTrackingState(isStart: Boolean){
        val notificationActionText = if(!isStart) "Pause" else "Resume"
        val pauseIntent = Intent(this, TimerService::class.java).apply{
            action = ACTION_START_SERVICE
        }
        val pendingIntent = PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        curNotificationBuilder.mActions.clear()

        curNotificationBuilder = baseNotificationBuilder
            .addAction(R.drawable.pomodoro, notificationActionText, pendingIntent)
        notificationManager.notify(NOTIFICATION_ID, curNotificationBuilder.build())


//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        curNotificationBuilder.javaClass.getDeclaredField("mAction").apply {
//            isAccessible = true
//            set(curNotificationBuilder, ArrayList<NotificationCompat.Action>())
//        }
//            curNotificationBuilder = baseNotificationBuilder.addAction(
//                R.drawable.add, notificationActionText, pendingIntent)
//            notificationManager.notify(NOTIFICATION_ID,curNotificationBuilder.build())

    }
    private fun stopService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

// Stop the service from running in the foreground
        stopForeground(true)

// Remove the notification
        notificationManager.cancel(NOTIFICATION_ID)

// Stop the service
//        stopSelf()
    }
}
package com.example.timesculptor.service

import android.animation.Animator
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Button
import com.example.timesculptor.MainActivity
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentFloatingWindowBinding
import com.example.timesculptor.service.TimerService.Companion.timeLeftFlow
import com.example.timesculptor.util.AppUtil.toMinutesSeconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class FloatingWindowService : Service() {
    private var windowManager: WindowManager? = null
    private var floatingView: View? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()


        val inflater = LayoutInflater.from(this)
        val binding = FragmentFloatingWindowBinding.inflate(inflater)
        floatingView = binding.root
        val lottieAnimationView = binding.lottieAnimationView
        lottieAnimationView.playAnimation()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
             WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_FULLSCREEN,
            PixelFormat.TRANSLUCENT
        )

        val flag = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

        binding.root.systemUiVisibility =flag
        windowManager?.addView(binding.root, params)



        params.gravity = Gravity.CENTER



        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                lottieAnimationView.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animator) {
                lottieAnimationView.playAnimation()
            }

            override fun onAnimationCancel(animation: Animator) {
                stopSelf()
            }

            override fun onAnimationRepeat(animation: Animator) {
                TODO("Not yet implemented")
            }

        })
        binding.quitTimer.setOnClickListener{
            val intent = Intent(this, TimerService::class.java)
            intent.also {
                it.putExtra("TIME_LEFT_IN_MILLIS", 10000L)
                it.action = "CANCEL_TIMER"
            }
            this.startService(intent)
            stopSelf()
        }


        GlobalScope.launch {
            timeLeftFlow.collectLatest {
                withContext(Main){
                    binding.timeLeft.text = it.toMinutesSeconds()
                    if(it < 100L){
                        stopSelf()
                    }
                }

            }
        }




        val closeButton = binding.close
        closeButton?.setOnClickListener {
            val intent = Intent(this@FloatingWindowService, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("LOAD_FRAGMENT", "pomodoro")
            lottieAnimationView.cancelAnimation()
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager?.removeView(floatingView)
    }
}

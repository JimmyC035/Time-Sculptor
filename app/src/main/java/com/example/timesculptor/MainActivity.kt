package com.example.timesculptor


import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.provider.Settings
import android.util.Log
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.work.WorkManager
import com.example.timesculptor.service.FloatingWindowService
import com.example.timesculptor.service.RebootService
import com.example.timesculptor.service.UnlockReceiver


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navBottomView: BottomNavigationView = findViewById(R.id.BottomNavigationView)
        val navController = navHostFragment.navController
        navBottomView.setupWithNavController(navController)


        if(intent != null){
//            val fragmentToLoad = intent.getStringExtra("LOAD_FRAGMENT")
//            if (fragmentToLoad == "pomodoro" && savedInstanceState == null) {
//                navController.navigate(R.id.action_navigate_to_pomodoro_Fragment)
//            }
        }


        //start service when first launch
        val pref = this.getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val isFirstRun = pref.getBoolean("first",true)
        val isFirstLaunch = pref.getBoolean("launch",false)
        val editor = pref.edit()

        if(isFirstLaunch){
            val serviceIntent = Intent(this, RebootService::class.java)
            startService(serviceIntent)

            editor.putBoolean("launch",false)
            editor.apply()
        }

        if(isFirstRun ||true){

            navController.navigate(R.id.action_navigate_to_welcome_Fragment)

            Log.i("share pref", pref.getBoolean("first",true).toString())
        }


        val lifecycleOwner: LifecycleOwner = this
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("WriteDBWorker")
            .observe(lifecycleOwner){
                it.forEach{workInfo ->
                    Log.i("MainWorker","${workInfo.state}")
                    Log.i("MainWorker","${workInfo.progress}")
                    Log.i("MainWorker","${workInfo.generation}")

                }
            }


        setupBottomNav()
    }

    private fun setupBottomNav() {
        binding.BottomNavigationView.setOnItemSelectedListener { item  ->
            when (item.itemId) {
                R.id.today -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_navigate_to_today_Fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.history -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_navigate_to_history_Fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.pomodoro -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_navigate_to_pomodoro_Fragment)
                    return@setOnItemSelectedListener true
                }

                R.id.home -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_navigate_to_home_Fragment)
                    return@setOnItemSelectedListener true
                }
                R.id.setting -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_navigate_to_user_setting_Fragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }



}
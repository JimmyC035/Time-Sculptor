package com.example.timesculptor

import android.content.ComponentName
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataManager = DataManager()



        if (!dataManager.hasUsagePermissionGranted(this)) {
            dataManager.requestUsagePermission(this)
        }

//        if(!isNotificationServiceEnabled()){
//            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
//            enableNotificationListenerAlertDialog.show();
//        }






        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navBottomView: BottomNavigationView = findViewById(R.id.BottomNavigationView)
        val navController = navHostFragment.navController
        navBottomView.setupWithNavController(navController)



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
            }
            false
        }
    }
//    private fun isNotificationServiceEnabled(): Boolean {
//        val pkgName = packageName
//        val flat = Settings.Secure.getString(
//            contentResolver,
//            ENABLED_NOTIFICATION_LISTENERS
//        )
//        if (!TextUtils.isEmpty(flat)) {
//            val names = flat.split(":".toRegex()).dropLastWhile { it.isEmpty() }
//                .toTypedArray()
//            for (i in names.indices) {
//                val cn = ComponentName.unflattenFromString(names[i])
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.packageName)) {
//                        return true
//                    }
//                }
//            }
//        }
//        return false
//    }

}
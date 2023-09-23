package com.example.timesculptor.today

import android.app.usage.EventStats
import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.homepage.HomeViewModel
import com.example.timesculptor.util.AppUtil.toHoursMinutes
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private val viewModel: TodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val usageTime = binding.totalUsage
        val todayDate = binding.todayDate
        val mostUsedIcon = binding.mostUsedIcon
        val mostUsedTime = binding.displayTime
        val notiToday = binding.notiCount
        val notiYesterday = binding.notiYesterdayCount
        val upOrDownArrow = binding.arrowIndicator
        val notiDiff = binding.twoDayDiff
        val pickUpCount = binding.pickUpCount
        val pickUpCountYesterday = binding.pickUpYesterdayCount
        val pickUPDiff = binding.twoDayDiffPickUp
        val pickUpIndicator = binding.pickUpIndicator
        val composeView = binding.composeView
        val goalAndUsage = binding.goalUsage
        val goalPickUP = binding.pickUpGoal


        //get total time
        viewModel.getUsage(requireContext())

        //get unlock count
        viewModel.testEventStatsForToday(requireContext())
        viewModel.testEventStatsForYesterday(requireContext())



        val mostUsed = viewModel.getMostUsedApp(requireContext())

        //set most used app icon
        val drawables = viewModel.getAppIcon(requireContext(),mostUsed.mPackageName)
        mostUsedIcon.setImageDrawable(drawables)

        mostUsedTime.text = mostUsed.mUsageTime.toHoursMinutesSeconds()

        viewModel.notiCount.observe(viewLifecycleOwner){
            notiToday.text = it.toString()
        }
        viewModel.notiYesterdayCount.observe(viewLifecycleOwner){
            notiYesterday.text = it.toString()
            if(viewModel.notiCount.value!! > it){
                upOrDownArrow.setImageResource(R.drawable.red_up)

            }else{
                upOrDownArrow.setImageResource(R.drawable.green_down)
            }
            notiDiff.text = abs(viewModel.notiCount.value!! - it).toString()
        }

        viewModel.pickUpCount.observe(viewLifecycleOwner){
            pickUpCount.text = it.toString()
        }

        viewModel.pickUpCountYesterday.observe(viewLifecycleOwner){
            pickUpCountYesterday.text = it.toString()
            if(viewModel.pickUpCount.value!! > it){
                pickUpIndicator.setImageResource(R.drawable.red_up)

            }else{
                pickUpIndicator.setImageResource(R.drawable.green_down)
            }
            pickUPDiff.text = abs(viewModel.pickUpCount.value!! - it).toString()
        }

        val pref = requireContext().getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val goal = pref.getLong("goal",2400000)



        viewModel.getUsage(requireContext())
        Log.i("goal",viewModel.totalTime.value.toString())
        val percentage:Float = viewModel.totalTime.value.toFloat() / goal.toFloat()
        val timeLeft = goal - viewModel.totalTime.value


        composeView.setContent {
            AnimatedCircle(percentage = percentage, timeLeft)
            Log.i("goal",goal.toHoursMinutesSeconds())
            Log.i("goal", percentage.toString())

        }

        goalAndUsage.text = "${viewModel.totalTime.value.toHoursMinutes()}" + " /" + goal.toHoursMinutes()

        goalPickUP.text = viewModel.pickUpCount.value.toString() + " /60"




            lifecycleScope.launch {
                viewModel.totalTime.collect{
                usageTime.text = it.toHoursMinutesSeconds()
                }
            }

        //get today date
        lifecycleScope.launch {
            viewModel.todayDate.collect{
                todayDate.text = it
            }
        }



        return binding.root
    }

}
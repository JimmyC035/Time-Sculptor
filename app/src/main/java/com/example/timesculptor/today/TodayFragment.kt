package com.example.timesculptor.today

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.yml.charts.common.model.Point
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.util.AppUtil.toHoursMinutes
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
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
        val lineChart = binding.lineChart
        val barChart = binding.barChart
        val mostUsedCard = binding.mostUsedCardview
        val pickUpCard = binding.pickUps
        val notificationCard = binding.notificationCard
        val goalAndPickUpCard = binding.goalAndPickUp
        notificationCard.visibility = GONE
        pickUpCard.visibility = GONE
        val slideInLeft = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left)
        val slideInRight = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        val slideInTop = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_top)
        val slideInLeftFast = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_left_fast)
        val slideInRightFast = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right_fast)

        val animationDuration = 900L

        mostUsedCard.startAnimation(slideInLeft)
        goalAndPickUpCard.startAnimation(slideInRight)
        barChart.startAnimation(slideInTop)

        slideInLeft.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    notificationCard.visibility = VISIBLE
                    pickUpCard.visibility = VISIBLE
                    notificationCard.startAnimation(slideInRightFast)
                    pickUpCard.startAnimation(slideInLeftFast)
                }, animationDuration / 2)
            }

            override fun onAnimationEnd(animation: Animation?) {
               Log.i("animation", "end")
            }

            override fun onAnimationRepeat(animation: Animation?) {
                TODO("Not yet implemented")
            }

        })

        goalAndPickUpCard.setOnClickListener{
            findNavController().navigate(R.id.action_navigate_to_user_setting_Fragment)
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.BottomNavigationView)
            bottomNav.selectedItemId = R.id.setting
        }




        //get total time
        viewModel.getUsage(requireContext())

        //get unlock count
        viewModel.testEventStatsForToday(requireContext())
        viewModel.testEventStatsForYesterday(requireContext())



        val mostUsed = viewModel.getMostUsedApp(requireContext())

        //set most used app icon
        if(mostUsed.mPackageName != ""){
            val drawables = viewModel.getAppIcon(requireContext(),mostUsed.mPackageName)
            mostUsedIcon.setImageDrawable(drawables)

            mostUsedCard.setOnClickListener{
                findNavController().navigate(TodayFragmentDirections.actionNavigateToDetailFragment(mostUsed.mPackageName))
            }
        }

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
            Log.i("pick up",it.toString())
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
        val goal = pref.getLong("goal",2400000L)
        val pickUp = pref.getInt("pickup",60)



//        viewModel.getUsage(requireContext())
        Log.i("goal",viewModel.totalTime.value.toString())
        val percentage:Float = viewModel.totalTime.value.toFloat() / goal.toFloat()
        val timeLeft = goal - viewModel.totalTime.value
        var percentageForPickup = 0f
        percentageForPickup = if(viewModel.pickUpCount.value == null){
            0f
        } else {
            viewModel.pickUpCount.value!!.toFloat().div(pickUp.toFloat())
        }


        composeView.setContent {
            AnimatedCircle(percentage = percentage, timeLeft,percentageForPickup)
            Log.i("goal",goal.toHoursMinutesSeconds())
            Log.i("goal", percentage.toString())

        }


        lifecycleScope.launch {
            viewModel.totalTime.collectLatest {
                if(it < 0L){
                    goalAndUsage.text = it.toHoursMinutes()  + " /" + goal.toHoursMinutes()
                }else{
                    goalAndUsage.text = it.toHoursMinutes()  + " /" + goal.toHoursMinutes()
                }
            }
        }
//        goalAndUsage.text = "${viewModel.totalTime.value.toHoursMinutes()}" + " /" + goal.toHoursMinutes()


        viewModel.pickUpCount.observe(viewLifecycleOwner){
            goalPickUP.text = it.toString() + " / $pickUp"
        }

//        goalPickUP.text = viewModel.pickUpCount.value.toString() + " / $pickUp"






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
        lifecycleScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val startOfDay = calendar.timeInMillis
            val current = System.currentTimeMillis()
            viewModel.getTillNow(startOfDay,current)
        }


      viewModel.charTillNow.observe(viewLifecycleOwner){ it ->
          val usageMap = mutableMapOf<Int, Float>()
          val sdf = SimpleDateFormat("HH", Locale.getDefault())
          sdf.timeZone = TimeZone.getDefault()



          it.forEach {item ->
              val date = Date(item.mEventTime - item.mUsageTime)
              val eventHour = sdf.format(date).toInt()
              val usageTimeInMinutes = item.mUsageTime.toFloat()

              usageMap[eventHour] = usageMap.getOrDefault(eventHour, 0f) + usageTimeInMinutes
          }

          val pointsData = (0..23).map { hour ->
               usageMap.getOrDefault(hour, 0f) / (1000f * 60f * 60f)
          }


          val yValuesList: List<Float> = viewModel.processList(pointsData)

          val xAxisScale = listOf("12AM", "1", "2", "3", "4", "5", "6AM", "7", "8", "9", "10", "11", "12PM", "1", "2", "3", "4", "5", "6PM", "7", "8", "9", "10", "11","12AM")


          lineChart.setContent {
              CustomChart(
                  barValue = yValuesList,
                  xAxisScale = xAxisScale,
                  total_amount = 60
              )
          }
      }


        return binding.root
    }

}
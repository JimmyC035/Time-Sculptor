package com.example.timesculptor.detail

import android.app.usage.UsageStatsManager
import android.content.Context
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import com.example.timesculptor.data.source.HomeItem
import com.example.timesculptor.databinding.FragmentDetailBinding
import com.example.timesculptor.databinding.FragmentPomodoroBinding
import com.example.timesculptor.pomodoro.PomodoroViewModel
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.SortEnum
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val chart = binding.lineChart
        val packageName = DetailFragmentArgs.fromBundle(requireArguments()).packageName
        Log.i("navigate?", packageName)
        val sessionData = viewModel.getTodayMockData(requireContext(),packageName)
        Log.i("session", sessionData.toString())

        val entries = mutableListOf<BarEntry>()



        val dataMap = mutableMapOf<Int, Float>().apply {
            for (i in 0..23) this[i] = 0f
        }

        sessionData.forEach { sessionData ->
            val calendar = Calendar.getInstance().apply {
                if (sessionData != null) {
                    timeInMillis = sessionData.eventTime
                }
            }
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            if (sessionData != null) {
                if (sessionData.eventType == 2) {
                    dataMap[hour] = dataMap[hour]!! + sessionData.duration/(1000 * 60)
                }
            }
        }

        dataMap.forEach { (hour, duration) ->
            entries.add(BarEntry(hour.toFloat(), duration))
        }

        val barDataSet = BarDataSet(entries, "Usage Time")
        val barData = BarData(barDataSet)
        val xAxis = chart.xAxis
        val yAxis = chart.axisLeft
        xAxis.textColor = Color.WHITE
        yAxis.textColor = Color.WHITE

        chart.data = barData
        chart.invalidate() // refresh chart





        return binding.root
    }

}
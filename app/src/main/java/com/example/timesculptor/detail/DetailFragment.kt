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
import com.example.timesculptor.util.AppUtil.getPackageIcon
import com.example.timesculptor.util.AppUtil.parsePackageName
import com.example.timesculptor.util.SortEnum
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
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
        val appIcon = binding.appIcon
        val chart = binding.lineChart
        val packageName = DetailFragmentArgs.fromBundle(requireArguments()).packageName
        Log.i("navigate?", packageName)
        val sessionData = viewModel.getTodayMockData(requireContext(),packageName)
        Log.i("session", sessionData.toString())

        val entries = mutableListOf<BarEntry>()
        val drawables = getPackageIcon(requireContext(),packageName)
        appIcon.setImageDrawable(drawables)
        binding.appName.text = parsePackageName(requireContext().packageManager,packageName)


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
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        val xAxis = chart.xAxis
        val yAxis = chart.axisLeft
        chart.legend.isEnabled = false
        chart.description.isEnabled = false
        xAxis.textColor = Color.WHITE
        yAxis.textColor = Color.WHITE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawLabels(true)
        chart.axisRight.setDrawGridLines(false)
        chart.axisRight.textColor = Color.TRANSPARENT
        chart.axisRight.isEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        yAxis.axisMaximum = 60f
        chart.animateXY(0, 2000)
        chart.setScaleEnabled(false)





        chart.data = barData
        chart.invalidate() // refresh chart





        return binding.root
    }
}
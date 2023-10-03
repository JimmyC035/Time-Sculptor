package com.example.timesculptor.history

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Canvas
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentBottomSheetDialogBinding
import com.example.timesculptor.databinding.FragmentHistoryBinding
import com.example.timesculptor.util.AppUtil.getPackageIcon
import com.example.timesculptor.util.AppUtil.toHoursMinutes
import com.example.timesculptor.util.AppUtil.toMonthAbbreviation
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class HistoryFragment : Fragment() {


    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val calendar = binding.calendarView









        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val startOfDay = calendar.timeInMillis
            val endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1

            viewModel.monthAndDate ="${month.toMonthAbbreviation()} $dayOfMonth"
            viewModel.loadDataForDate(startOfDay,endOfDay)

        }
        viewModel.topUsed.observe(viewLifecycleOwner) { topUsed ->


            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
            val mostUsedIcon = binding.mostUsedIcon
            if(topUsed != null){
                val drawables = getPackageIcon(requireContext(),topUsed.packageName)
                mostUsedIcon.setImageDrawable(drawables)
                binding.displayTime.text = topUsed.totalUsageTime.toHoursMinutes()
                binding.launchCount.text = (if(topUsed.totalUsageCount == 0){
                    "1"
                }else{
                    (topUsed.totalUsageCount + 1).toString()
                })
            }else{
                binding.displayTime.text = 0L.toHoursMinutes()
                binding.launchCount.text = "0"
            }
            if(viewModel.totalUsageSelectedDate.value != null){
                binding.totalUsage.text = viewModel.totalUsageSelectedDate.value!!.toHoursMinutes()
            }
            if(viewModel.notificationCount.value != null){
                binding.notiCount.text = viewModel.notificationCount.value.toString()
            }else{
                binding.notiCount.text = "0"
            }
            binding.date.text = viewModel.monthAndDate




            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.show()
        }



        return binding.root
    }
}

package com.example.timesculptor.history

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
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
import com.example.timesculptor.databinding.DialogAddBinding
import com.example.timesculptor.databinding.FragmentBottomSheetDialogBinding
import com.example.timesculptor.databinding.FragmentHistoryBinding
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.homepage.HomeViewModel
import com.example.timesculptor.service.ACTION_START_SERVICE
import com.example.timesculptor.service.TimerService
import com.example.timesculptor.today.TodayViewModel
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

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val startOfDay = calendar.timeInMillis
            val endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1

            lifecycleScope.launch {
                viewModel.getTotalUsageForDate(startOfDay,endOfDay)
                Log.i("total usage", "function called")
            }



        }
        viewModel.totalUsageSelectedDate.observe(viewLifecycleOwner) { totalUsage ->
//            if (totalUsage != null) {
//                Log.i("total usage", totalUsage.toString())
//
//            } else {
//                Log.i("total usage", " NO DATA")
//            }
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val binding = FragmentBottomSheetDialogBinding.inflate(inflater, container, false)
            if(totalUsage != null){
                binding.totalUsage.text = totalUsage.toString()
            }
            binding.textView3.text = "SEP 26"


            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.show()

        }



        return binding.root
    }
}

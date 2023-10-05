package com.example.timesculptor.setting

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.content.SharedPreferences
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.databinding.FragmentUserSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class UserSettingFragment : Fragment() {

    private val viewModel: UserSettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserSettingBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val notificationTimePickBtn = binding.button2
        val notificationTime = binding.textView2
//        val goalPicker = binding.setGoal
        val goalPickerBtn = binding.button
        val pref = requireContext().getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val editor = pref.edit()



        notificationTimePickBtn.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting our hour, minute.
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)


            // initialize our Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->

                    notificationTime.text = "receive your daily report around $hourOfDay:$minute"
                    editor.putInt("notification_hour", hourOfDay)
                    editor.putInt("notification_minute", minute)
                    editor.apply()

                    viewModel.updateWorker(requireContext(), hourOfDay, minute)

                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }


        val hourPicker = binding.timePickHour
        hourPicker.minValue = 0
        hourPicker.maxValue = 24
        hourPicker.value = 0


        val minutePicker =  binding.timePickMin
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        minutePicker.value = 30



        fun saveToSharedPreferences() {
            val selectedHour = hourPicker.value
            val selectedMinute = minutePicker.value

            val selectedToLong = (selectedHour.toLong() * 1000 * 60 * 60) + (selectedMinute.toLong() * 1000 * 60)

            editor.putLong("goal", selectedToLong)

            editor.apply()
        }




        goalPickerBtn.setOnClickListener {
            saveToSharedPreferences()
        }




        return binding.root
    }

}
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
        val goalPicker = binding.setGoal
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

            // on below line we are initializing
            // our Time Picker Dialog
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


        goalPickerBtn.setOnClickListener {

            val goalTime = goalPicker.text.toString().toLong()
            editor.putLong("goal", goalTime)
            editor.apply()


            val goal = pref.getLong("goal", 0)
            val notiTime = pref.getInt("notification_hour", 0)
            Log.i("goal", "goal = $goal")
            Log.i("goal", "notification time  = $notiTime")
        }




        return binding.root
    }

}
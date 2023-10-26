package com.example.timesculptor.setting

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import android.app.Dialog
import com.example.timesculptor.databinding.FragmentUserSettingBinding
import com.example.timesculptor.databinding.GoalPickerBinding
import com.example.timesculptor.databinding.PickUpGoalSettingBinding
import com.example.timesculptor.util.AppConst.DEFAULT_WIDTH
import com.example.timesculptor.util.AppUtil.toHoursMinutes
import com.example.timesculptor.util.AppUtil.toMinutesSeconds
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
        val dailyReportSetting = binding.dailyReportSetting
        val dailyReportResult = binding.dailyReportResult
        val goalPickerBtn = binding.goalSetting
        val usageGoalResult = binding.goalResult
        val pickUpResult = binding.pickUpResult
        val pref = requireContext().getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val editor = pref.edit()

        val goalDialogView = GoalPickerBinding.inflate(inflater, container, false)
        val hourPicker = goalDialogView.hourPicker
        val minPicker = goalDialogView.minutePicker
        hourPicker.minValue = 1
        hourPicker.maxValue = 24
        minPicker.minValue = 0
        minPicker.maxValue = 59
        val dialogUsage = AlertDialog.Builder(context)
            .setView(goalDialogView.root)
            .create()

        val pickUpGoalSetting = binding.pickUpSetting
        val pickUpGoalDialog = PickUpGoalSettingBinding.inflate(inflater, container, false)
        val pickUpGoal = pickUpGoalDialog.pickUpPicker
        pickUpGoal.minValue = 1
        pickUpGoal.maxValue = 300
        pickUpGoal.value = 60


        val hourOfDay = pref.getInt("notification_hour",8)
        val minutes = pref.getInt("notification_minute",30)
        dailyReportResult.text = "Your daily report will arrive around $hourOfDay:$minutes"

        val hours = pref.getLong("goal",240000L)
        usageGoalResult.text = "${hours.toHoursMinutes()} per day"


        val pickResult = pref.getInt("pickup",60)
        pickUpResult.text = "$pickResult times per day"


        val dialogPickUp = AlertDialog.Builder(context)
            .setView(pickUpGoalDialog.root)
            .create()





        dailyReportSetting.setOnClickListener {
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

                    dailyReportResult.text =
                        "Your daily report will arrive around $hourOfDay:$minute"
                    editor.putInt("notification_hour", hourOfDay)
                    editor.putInt("notification_minute", minute)
                    editor.apply()

                    viewModel.updateWorker(requireContext(), hourOfDay, minute)
//                    dailyReportResult.text = "Your daily report will arrive around $hourOfDay:$minutes"

                },
                hour,
                minute,
                false
            )

            timePickerDialog.show()
        }

        goalPickerBtn.setOnClickListener {
            dialogUsage.setOnShowListener {
                val window = (it as Dialog).window
                window?.attributes = window?.attributes?.apply {
                    width =
                        (context?.resources?.displayMetrics?.widthPixels?.times(0.85))?.toInt()?: DEFAULT_WIDTH
                }
                window?.setBackgroundDrawableResource(R.drawable.dialog_background)
            }

        dialogUsage.show()

        goalDialogView.confirmBtn.setOnClickListener {
            val selectedHour = hourPicker.value
            val selectedMinute = minPicker.value
            val selectedToLong =
                (selectedHour.toLong() * 1000 * 60 * 60) + (selectedMinute.toLong() * 1000 * 60)
            editor.putLong("goal", selectedToLong)
            Log.i("goal setting", "$selectedToLong")
            usageGoalResult.text = "${selectedToLong.toHoursMinutes()} per day"
            editor.apply()
            dialogUsage.dismiss()
        }
    }

    pickUpGoalSetting.setOnClickListener{
        dialogPickUp.setOnShowListener {
            val window = (it as Dialog).window
            window?.attributes = window?.attributes?.apply {
                width = (context?.resources?.displayMetrics?.widthPixels?.times(0.75))?.toInt()?: DEFAULT_WIDTH
            }
            window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        }
        dialogPickUp.show()

        pickUpGoalDialog.confirmBtn.setOnClickListener {
            val pickUpGoal = pickUpGoal.value
            editor.putInt("pickup", pickUpGoal)
            Log.i("goal pick up setting", "$pickUpGoal")
            pickUpResult.text = "$pickUpGoal times per day"
            editor.apply()
            dialogPickUp.dismiss()
        }
    }




    return binding.root
}

}
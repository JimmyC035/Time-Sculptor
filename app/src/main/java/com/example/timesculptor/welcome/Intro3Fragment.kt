package com.example.timesculptor.welcome

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.timesculptor.R
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.Intro3FragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Intro3Fragment : Fragment() {

    private val viewModel: Intro3ViewModel by viewModels()
    private val dataManager = DataManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Intro3FragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val usagePermission = binding.permission1
        val overlayPermission = binding.permission2
        val notificationPermission = binding.permission3
        val go = binding.go
        go.alpha = 0.5f
        go.isEnabled = false
        val pref = requireContext().getSharedPreferences("my_setting", Context.MODE_PRIVATE)
        val isFirstRun = pref.getBoolean("first",true)
        val editor = pref.edit()
        usagePermission.setOnClickListener{
            Log.i("userPermission","clicked!")
            if (!dataManager.hasUsagePermissionGranted(requireContext()) ||true) {
                dataManager.requestUsagePermission(requireContext())
            }
        }

        overlayPermission.setOnClickListener{
            Log.i("userPermission","clicked!2")
            val permission = Manifest.permission.SYSTEM_ALERT_WINDOW
            if (!Settings.canDrawOverlays(requireContext())  ||true) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + requireActivity().packageName))
                startActivityForResult(intent, 123)
            }
        }
        notificationPermission.setOnClickListener{
            Log.i("userPermission","clicked!3")
            if(!dataManager.isNotificationAccessGranted(requireContext())  ||true){
                dataManager.requestNotificationAccess(requireContext())
            }
        }


        lifecycleScope.launch {
            viewModel.usagePermission.collectLatest { granted ->
                if (granted) {
                   binding.usageCheckbox.setImageResource(R.drawable.baseline_check_24)
                }else{
                    binding.usageCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.overlayPermission.collectLatest { granted ->
                Log.i("userPermission","overlay called?")
                if (granted) {
                    binding.overlayCheckbox.setImageResource(R.drawable.baseline_check_24)
                }else{
                    binding.overlayCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.notificationPermission.collectLatest { granted ->
                if (granted) {
                    binding.notificationCheckbox.setImageResource(R.drawable.baseline_check_24)
                }else{
                    binding.notificationCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.combinedFlow.collectLatest {allTrue ->
                go.alpha = 1f
                go.isEnabled = allTrue
            }
        }

        go.setOnClickListener{
            findNavController().navigate(R.id.action_navigate_to_home_Fragment)
            editor.putBoolean("first",false)
            editor.apply()
            Log.i("sharepref", pref.getBoolean("first",true).toString())
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkAndUpdateUsagePermissionState(requireContext(),dataManager)
        viewModel.checkAndUpdateOverlayPermissionState(requireContext())
        viewModel.checkAndUpdateNotificationPermissionState(requireContext(), dataManager)
    }
}
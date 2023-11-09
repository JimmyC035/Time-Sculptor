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
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.timesculptor.R
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.Intro3FragmentBinding
import com.example.timesculptor.util.AppUtil.scrollToBottom
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Intro3Fragment : Fragment() {

    private val viewModel: Intro3ViewModel by viewModels()
    private val dataManager = DataManager()
    private lateinit var usagePermission: MaterialCardView
    private lateinit var overlayPermission: MaterialCardView
    private lateinit var notificationPermission: MaterialCardView
    lateinit var go: AppCompatButton
    lateinit var fadeIn1: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Intro3FragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        usagePermission = binding.permission1
        overlayPermission = binding.permission2
        notificationPermission = binding.permission3
        go = binding.go
        go.alpha = 0.5f
        go.isEnabled = false
        val pref = requireContext().getSharedPreferences("my_setting", Context.MODE_PRIVATE)
//        val isFirstRun = pref.getBoolean("first",true)
        val editor = pref.edit()
        val scrollView = binding.scrollView



        fadeIn1 = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val fadeIn2 = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val fadeIn3 = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        fadeIn1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                overlayPermission.visibility = GONE
                notificationPermission.visibility = GONE
                go.visibility = GONE
            }

            override fun onAnimationEnd(animation: Animation?) {
                overlayPermission.visibility = VISIBLE
                overlayPermission.startAnimation(fadeIn2)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        fadeIn2.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                notificationPermission.visibility = VISIBLE
                notificationPermission.startAnimation(fadeIn3)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        fadeIn3.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                go.visibility = VISIBLE
                scrollView.scrollToBottom()

            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })






        usagePermission.setOnClickListener {
            Log.i("userPermission", "clicked!")
            dataManager.requestUsagePermission(requireContext())
        }

        overlayPermission.setOnClickListener {
            Log.i("userPermission", "clicked!2")
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().packageName)
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityForResult(intent, 123)

        }
        notificationPermission.setOnClickListener {
            Log.i("userPermission", "clicked!3")

            dataManager.requestNotificationAccess(requireContext())

        }


        lifecycleScope.launch {
            viewModel.usagePermission.collectLatest { granted ->
                if (granted) {
                    binding.usageCheckbox.setImageResource(R.drawable.baseline_check_24)
                } else {
                    binding.usageCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.overlayPermission.collectLatest { granted ->
                Log.i("userPermission", "overlay called?")
                if (granted) {
                    binding.overlayCheckbox.setImageResource(R.drawable.baseline_check_24)
                } else {
                    binding.overlayCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.notificationPermission.collectLatest { granted ->
                if (granted) {
                    binding.notificationCheckbox.setImageResource(R.drawable.baseline_check_24)
                } else {
                    binding.notificationCheckbox.setImageDrawable(null)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.combinedFlow.collectLatest { allTrue ->
                go.alpha = 1f
                go.isEnabled = allTrue
            }
        }

        go.setOnClickListener {
            findNavController().navigate(R.id.action_navigate_to_home_Fragment)
            editor.putBoolean("first", false)
            editor.apply()
            Log.i("sharepref", pref.getBoolean("first", true).toString())
        }



        return binding.root
    }

    fun startAnimationSequence() {
        usagePermission.startAnimation(fadeIn1)
        go.visibility = GONE
        overlayPermission.visibility = GONE
        notificationPermission.visibility = GONE
    }


    override fun onResume() {
        super.onResume()
        viewModel.checkAndUpdateUsagePermissionState(requireContext(), dataManager)
        viewModel.checkAndUpdateOverlayPermissionState(requireContext())
        viewModel.checkAndUpdateNotificationPermissionState(requireContext(), dataManager)
    }
}
package com.example.timesculptor.pomodoro

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentPomodoroBinding
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.today.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PomodoroFragment : Fragment() {

    private val viewModel: PomodoroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPomodoroBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        return binding.root
    }

}
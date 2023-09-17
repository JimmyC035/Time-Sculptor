package com.example.timesculptor.today

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.homepage.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private val viewModel: TodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        return binding.root
    }

}
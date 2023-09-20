package com.example.timesculptor.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentDetailBinding
import com.example.timesculptor.databinding.FragmentPomodoroBinding
import com.example.timesculptor.pomodoro.PomodoroViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        val packageName = DetailFragmentArgs.fromBundle(requireArguments()).packageName
        Log.i("navigate?","$packageName")


        return binding.root
    }

}
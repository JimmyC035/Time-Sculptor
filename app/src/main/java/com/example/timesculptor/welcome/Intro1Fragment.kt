package com.example.timesculptor.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timesculptor.databinding.Intro1FragmentBinding
import com.example.timesculptor.databinding.Intro2FragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Intro1Fragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = Intro1FragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}
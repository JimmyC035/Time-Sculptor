package com.example.timesculptor.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentBottomSheetDialogBinding
import com.example.timesculptor.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetDialogFragment : Fragment()
{

    private val viewModel: BottomSheetDialogViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val binding = FragmentBottomSheetDialogBinding.inflate(inflater,container,false)
            binding.lifecycleOwner = viewLifecycleOwner
            binding.viewModel = viewModel

            return binding.root
        }



}
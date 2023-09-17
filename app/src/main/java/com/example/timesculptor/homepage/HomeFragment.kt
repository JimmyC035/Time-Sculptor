package com.example.timesculptor.homepage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        Log.i("bbt","here?")
        val composeView = binding.composeView

        //should be moved to repo
        val dataManager = DataManager()
//        val listItem = dataManager.getTargetAppTimeline(requireContext(),"com.google.android.youtube",3)
//        Log.i("session","$listItem")
//        val listItem = dataManager.getAll(requireContext(),0)
        val listItem = dataManager.getApps(requireContext(),0)
        Log.i("session","$listItem")


//
        composeView.setContent {
            if(listItem.size >= 5){
                PieChart(
                    data = listItem.take(5).associate {
                        val item = it
                        Pair(item.mName, item.mUsageTime)
                    }
                )
            }else{
                PieChart(
                    data = listItem.indices.associate {
                        val item = listItem[it]
                        Pair(item.mName, item.mUsageTime)
                    }
                )
            }

        }



        viewModel.testEventStats(requireContext())

        Log.i("bbt","call?")




        return binding.root
    }



}
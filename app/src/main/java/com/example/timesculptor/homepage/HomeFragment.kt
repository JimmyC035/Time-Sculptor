package com.example.timesculptor.homepage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
//        val composeView = binding.composeView

        //should be moved to repo
        val dataManager = DataManager()
//        val listItem = dataManager.getTargetAppTimeline(requireContext(),"com.google.android.youtube",3)
//        Log.i("session","$listItem")
        val listItem = dataManager.getAll(requireContext(),3)
//        val listItem = dataManager.getApps(requireContext(),0,0)
        Log.i("session","$listItem")

//
//        composeView.setContent {
//            PieChart(
//                data = mapOf(
//                    Pair(listItem[0].packageName,listItem[0].usageTime.toInt()),
//                    Pair(listItem[1].packageName,listItem[1].usageTime.toInt()),
//                    Pair(listItem[2].packageName,listItem[2].usageTime.toInt()),
//                    Pair(listItem[3].packageName,listItem[3].usageTime.toInt()),
//                    Pair(listItem[4].packageName,listItem[4].usageTime.toInt()),
//                )
//            )
//        }


        viewModel.testEventStats(requireContext())

        Log.i("bbt","call?")




        return binding.root
    }



}
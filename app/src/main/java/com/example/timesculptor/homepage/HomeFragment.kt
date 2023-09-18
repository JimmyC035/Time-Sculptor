package com.example.timesculptor.homepage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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


        // for testing insert dao
//        val noti = NotificationHistory(listItem[0].mPackageName,listItem[0].mName,listItem[0].mEventTime)
//        lifecycleScope.launch {
//            viewModel.testInsert(noti)
//        }
//
//        val youtube = dataManager.getTargetAppTimeline(requireContext(),"com.example.timesculptor",0)
//        Log.i("youtube","$youtube")

        //for testing db
//        lifecycleScope.launch {
//            viewModel.testForInsert(dataManager.getApps(requireContext(),0))
//        }

        viewModel.doWork(requireContext())
        viewModel.doDBWork(requireContext())

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
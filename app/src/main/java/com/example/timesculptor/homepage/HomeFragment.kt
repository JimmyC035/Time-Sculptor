package com.example.timesculptor.homepage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.example.timesculptor.R
import com.example.timesculptor.data.source.DataManager
import com.example.timesculptor.data.source.NotificationHistory
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

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
        val usageTime = binding.totalUsage
        val usageTimeInCircle = binding.circleTime
        val homeDate = binding.homeDate
        val composeView = binding.composeView




        //get data
        val listItem = viewModel.getHomePageData(requireContext())
        Log.i("session","$listItem")


        //can't refresh due to function only call while viewModel init

            viewModel.notiCount.observe(viewLifecycleOwner){
                Log.i("noti", it.toString())
            }

        val myClick : (String) -> Unit = { packageName: String ->
            findNavController().navigate(HomeFragmentDirections.actionNavigateToDetailFragment(packageName))
        }


        //get total time
        viewModel.getUsage(requireContext())

        lifecycleScope.launch {
            viewModel.totalTime.collect{
                usageTime.text = it.toHoursMinutesSeconds()
                usageTimeInCircle.text = it.toHoursMinutesSeconds()
            }
        }

        //get today date
        lifecycleScope.launch {
            viewModel.todayDate.collect{
                homeDate.text = it
            }
        }




        composeView.setContent {
            if(listItem.size >= 5){

                PieChart(
                    data = listItem.take(5).associate {
                        val item = it
                        Pair(item.mName, item.mUsageTime)
                    },
                    icon = listItem.take(5).map {
                       viewModel.getAppIcon(requireContext(),it.mPackageName)!!
                    },
                    packageName = listItem.take(5).map{
                        it.mPackageName
                    },
                    myClick


                )
            }else{
                PieChart(
                    data = listItem.indices.associate {
                        val item = listItem[it]
                        Pair(item.mName, item.mUsageTime)
                    },
                    icon = listItem.indices.map {
                        val item = listItem[it]
                        viewModel.getAppIcon(requireContext(),item.mPackageName)!!
                    },
                    packageName = listItem.indices.map{
                        val item = listItem[it]
                        item.mPackageName
                    },
                    myClick
                )
            }

        }



//        viewModel.testEventStats(requireContext())

        Log.i("bbt","call?")




        return binding.root
    }



}
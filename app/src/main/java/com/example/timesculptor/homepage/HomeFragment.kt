package com.example.timesculptor.homepage

import android.graphics.drawable.Drawable
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
import com.example.timesculptor.data.source.PieChartDataset
import com.example.timesculptor.databinding.FragmentHomeBinding
import com.example.timesculptor.util.AppUtil
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val usageTime = binding.totalUsage
        val usageTimeInCircle = binding.circleTime
        val homeDate = binding.homeDate
        val composeView = binding.composeView

        //get data
        viewModel.getHomePageData(requireContext())

        val myClick: (String) -> Unit = { packageName: String ->
            findNavController().navigate(
                HomeFragmentDirections.actionNavigateToDetailFragment(
                    packageName
                )
            )
        }

        //get total time
        viewModel.pieChartDataset.observe(viewLifecycleOwner) { dataset ->
            composeView.setContent {
                PieChart(
                    data = dataset.dataMap,
                    icon = dataset.icons,
                    packageName = dataset.packageNames,
                    myClick
                )
            }
        }



        WorkManager.getInstance(requireContext()).cancelUniqueWork("SendNotificationWorker")

        lifecycleScope.launch {
            viewModel.totalTime.collect {
                usageTime.text = it.toHoursMinutesSeconds()
                usageTimeInCircle.text = it.toHoursMinutesSeconds()
            }
        }

        //get today date
        lifecycleScope.launch {
            viewModel.todayDate.collect {
                homeDate.text = it
            }
        }

        //update DB
        lifecycleScope.launch {
            viewModel.updateDb(requireContext())
        }

        return binding.root
    }
}



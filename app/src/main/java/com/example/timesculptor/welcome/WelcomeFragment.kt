package com.example.timesculptor.welcome

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentUserSettingBinding
import com.example.timesculptor.databinding.FragmentWelcomeBinding
import com.example.timesculptor.setting.UserSettingViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

private const val PAGE_COUNT = 3


private const val PAGE_ONE = 0
private const val PAGE_TWO = 1
private const val PAGE_THREE = 2

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        val viewPager  = binding.viewPager2

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int): Fragment {
                return   when (position) {
                    PAGE_ONE -> Intro1Fragment()
                    PAGE_TWO -> Intro2Fragment()
                    PAGE_THREE -> Intro3Fragment()
                    else -> Intro1Fragment()
                }
            }
        }
        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(this.requireContext(), R.drawable.pomodoro)
        }.attach()




        return binding.root
    }

}
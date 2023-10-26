package com.example.timesculptor.homepage

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.example.timesculptor.data.source.source.AppItem
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @Mock
    lateinit var mockRepo: TimeSculptorRepository


    private lateinit var viewModel: HomeViewModel

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(mockRepo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetUsageUpdatesTotalTime() = runTest(UnconfinedTestDispatcher()) {
        // mock data
        val testData = listOf(
            AppItem("App1", "com.example.app1", mUsageTime = 1000L),
            AppItem("App2", "com.example.app2", mUsageTime = 2000L),
            AppItem("App3", "com.example.app3", mUsageTime = 3000L)
        )

        launch { viewModel.getUsage(testData) }
        val expectedTotalTime = 6000L
        assertEquals(expectedTotalTime, viewModel.totalTime.value)

    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.O_MR1])
    fun testGetAppIcon() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val packageName = "com.example.timesculptor"
        val packageManager = context.packageManager
        val result = viewModel.getAppIcon(context, packageName)

        assertEquals(packageManager.getApplicationIcon(context.packageName), result)
    }

}
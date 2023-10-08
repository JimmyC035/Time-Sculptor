package com.example.timesculptor.pomodoro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.timesculptor.R
import com.example.timesculptor.service.FloatingWindowService
import com.example.timesculptor.service.TimerService
import com.example.timesculptor.service.TimerService.Companion.isTimerRunningFlow
import com.example.timesculptor.service.TimerService.Companion.timeLeftFlow
import com.example.timesculptor.service.TimerService.Companion.totalTime
import com.example.timesculptor.util.AppConst.DEFAULT_TIME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.sqrt

@AndroidEntryPoint
class PomodoroFragment : Fragment() {

    private val viewModel: PomodoroViewModel by viewModels()

    private var isTimerRunning = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                isTimerRunningFlow.collectLatest {
                    isTimerRunning = it
                }
            }
        }

        return ComposeView(requireContext()).apply {

            setContent {

                Surface(
                    color = Color(0xFF222627),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(bottom = 90.dp)
                    ) {
                        Surface(
                            color = Color(0xFF222627),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                val state = totalTime.collectAsState()
                                val totalTime = state.value
                                val inactiveBarColor = Color.DarkGray
                                val activeBarColor = Color(0xBBFF6347)
                                val modifier = Modifier.size(250.dp)
                                val initialValue: Float = 1f
                                val strokeWidth: Dp = 20.dp
                                var size by remember {
                                    mutableStateOf(IntSize.Zero)
                                }
                                var value by remember {
                                    mutableStateOf(initialValue)
                                }

                                var isRunning = isTimerRunningFlow.collectAsState().value
                                val currentTime = timeLeftFlow.collectAsState().value


                                val onDecreaseClick = {
                                    if (!isRunning) {
                                        viewModel.minusTime()
                                        if (viewModel.currentTime.value < 0) viewModel.resetTimer()
                                    }
                                }
                                val onIncreaseClick = {
                                    if (!isRunning) {
                                        viewModel.addTime()
                                    }
                                }

                                LaunchedEffect(key1 = currentTime, key2 = isRunning) {
                                    if (currentTime > 0L && isRunning) {
                                        value = currentTime / totalTime.toFloat()
                                        Log.i("launnched", "$currentTime | $totalTime")
                                    }
                                    if (currentTime <= 100L) {
                                        value = 1f
                                    }
                                    // problem solved but little chance of encounter error
                                    if(isRunning.not() && currentTime == DEFAULT_TIME){
                                        value = 1f
                                    }

                                }


                                val minutes = (currentTime % 3600000) / 60000
                                val seconds = (currentTime % 60000) / 1000
                                val viewModelTimeState = viewModel.currentTime.collectAsState()
                                val minutesInViewModel =
                                    (viewModelTimeState.value % 3600000) / 60000
                                val secondsInViewModel = (viewModelTimeState.value % 60000) / 1000
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = modifier
                                        .onSizeChanged {
                                            size = it
                                        }
                                ) {
                                    Canvas(modifier = modifier) {
                                        drawArc(
                                            color = inactiveBarColor,
                                            startAngle = -90f,
                                            sweepAngle = 360f,
                                            useCenter = false,
                                            size = Size(
                                                size.width.toFloat(),
                                                size.height.toFloat()
                                            ),
                                            style = Stroke(
                                                strokeWidth.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        )
                                        drawArc(
                                            color = activeBarColor,
                                            startAngle = -90f,
                                            sweepAngle = 360f * value,
                                            useCenter = false,
                                            size = Size(
                                                size.width.toFloat(),
                                                size.height.toFloat()
                                            ),
                                            style = Stroke(
                                                strokeWidth.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.align(Alignment.Center)
                                    ) {
                                        val modifier = Modifier
                                        val rotationAngle = -90f
                                        Canvas(
                                            modifier = modifier
                                                .clickable {
                                                    onDecreaseClick()
                                                }
                                                .size(30.dp, 30.dp)) {
                                            val buttonSize = with(LocalDensity) { 10.dp.toPx() }
                                            val triangleHeight = buttonSize * sqrt(3f) / 2f
                                            val offset = 10.dp.toPx()
                                            drawContext.canvas.save()


                                            drawContext.canvas.rotate(
                                                rotationAngle,
                                                center.x - offset,
                                                center.y
                                            )
                                            val trianglePath = Path().apply {
                                                moveTo(
                                                    center.x - offset,
                                                    center.y - buttonSize / 2
                                                )
                                                lineTo(
                                                    center.x - offset - triangleHeight / 2,
                                                    center.y + buttonSize / 2
                                                )
                                                lineTo(
                                                    center.x - offset + triangleHeight / 2,
                                                    center.y + buttonSize / 2
                                                )
                                                close()
                                            }
                                            drawPath(
                                                path = trianglePath,
                                                color = Color.White,
                                                style = Fill,

                                                )
                                            drawContext.canvas.restore()
                                        }
                                        Text(
                                            text = if (isRunning.not()) {
                                                String.format(
                                                    "%02d:%02d",
                                                    minutesInViewModel,
                                                    secondsInViewModel
                                                )
                                            } else {
                                                String.format("%02d:%02d", minutes, seconds)
                                            },
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        Canvas(
                                            modifier = modifier
                                                .clickable {
                                                    onIncreaseClick()
                                                }
                                                .size(30.dp, 30.dp)) {
                                            val buttonSize = with(LocalDensity) { 10.dp.toPx() }
                                            val triangleHeight = buttonSize * sqrt(3f) / 2f
                                            val offset = 10.dp.toPx()
                                            drawContext.canvas.save()
                                            drawContext.canvas.rotate(
                                                90f,
                                                center.x + offset,
                                                center.y
                                            )
                                            val trianglePath = Path().apply {
                                                moveTo(center.x + offset, center.y - buttonSize / 2)
                                                lineTo(
                                                    center.x + offset - triangleHeight / 2,
                                                    center.y + buttonSize / 2
                                                )
                                                lineTo(
                                                    center.x + offset + triangleHeight / 2,
                                                    center.y + buttonSize / 2
                                                )
                                                close()
                                            }
                                            drawPath(
                                                path = trianglePath,
                                                color = Color.White,
                                                style = Fill,
                                            )

                                            drawContext.canvas.restore()
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(top = 32.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.cancelTimer(requireContext())
                                            viewModel.resetTimer()
                                            value = 1f
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.White,
                                            contentColor = Color.Black
                                        )
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_stop_24),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()

                                        )
                                    }

                                    Spacer(modifier = Modifier.size(32.dp))

                                    Button(
                                        onClick = {
                                            if (isRunning) {
                                                viewModel.pauseTimer(requireContext())
                                            } else {
                                                viewModel.startTimer(requireContext())
                                            }

                                        },
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (!isRunning || currentTime <= 0L) {
                                                Color(0xBBFF6347)
                                            } else {
                                                Color(0xFF4777AF)
                                            }
                                        )
                                    ) {
                                        val imageResource =
                                            if (isRunning) {
                                                R.drawable.baseline_pause_24
                                            } else {
                                                R.drawable.baseline_play_arrow_24
                                            }

                                        Image(
                                            painter = painterResource(id = imageResource),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()

                                        )
                                    }

                                    Spacer(modifier = Modifier.size(32.dp))

                                    Button(
                                        onClick = {
                                            viewModel.resetTimer()
                                            viewModel.cancelTimer(requireContext())
                                            value = 1f
                                        },
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.White,
                                            contentColor = Color.Black
                                        ),

                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.baseline_refresh_24),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier

                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        if (isTimerRunning) {
            val intent = Intent(requireContext(), FloatingWindowService::class.java)
            requireContext().startService(intent)
        }
        super.onPause()
    }
}









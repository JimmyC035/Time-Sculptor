package com.example.timesculptor.pomodoro

import android.content.Intent
import android.os.Binder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesculptor.R
import com.example.timesculptor.databinding.FragmentPomodoroBinding
import com.example.timesculptor.databinding.FragmentTodayBinding
import com.example.timesculptor.service.FloatingWindowService
import com.example.timesculptor.today.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlin.math.sqrt

@AndroidEntryPoint
class PomodoroFragment : Fragment() {

    private val viewModel: PomodoroViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val timerState by viewModel.timerState.collectAsState()
                Surface(
                    color = Color(0xFF101010),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Color(0xFF101010),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {

                                val totalTime = timerState.totalTime
                                val inactiveBarColor = Color.DarkGray
                                val activeBarColor = Color(0xFFB3B5EE)
                                val modifier = Modifier.size(250.dp)
                                val initialValue: Float = 1f
                                val strokeWidth: Dp = 20.dp
                                var size by remember {
                                    mutableStateOf(IntSize.Zero)
                                }
                                var value by remember {
                                    mutableStateOf(initialValue)
                                }

                                var isTimerRunning by remember {
                                    mutableStateOf(false)
                                }

                                var currentTime by remember {
                                    mutableStateOf(totalTime)
                                }

                                LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
                                    if (currentTime > 0 && isTimerRunning) {
                                        delay(100L)
                                        currentTime -= 100L
                                        value = currentTime / totalTime.toFloat()
                                    }

                                }


                                val minutes = (currentTime % 3600000) / 60000
                                val seconds = (currentTime % 60000) / 1000
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
                                            startAngle = 0f,
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
                                            startAngle = 0f,
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
                                                    viewModel.onDecreaseClicked()
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
                                            text = String.format("%02d:%02d", minutes, seconds),
                                            fontSize = 44.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        Canvas(
                                            modifier = modifier
                                                .clickable {
                                                    viewModel.onIncreaseClicked()
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

                                    Button(
                                        onClick = {
                                            viewModel.onStartStopClicked()
                                        },
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 32.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                                                Color(0xFFB3B5EE)
                                            } else {
                                                Color(0xFF4777AF)
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = if (isTimerRunning && currentTime >= 0L) "Stop"
                                            else if (!isTimerRunning && currentTime >= 0L) "Start"
                                            else "Restart"
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
        val intent = Intent(requireContext(), FloatingWindowService::class.java)
        requireContext().startService(intent)
        super.onPause()
    }
}









//package com.example.timesculptor.pomodoro
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Fill
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.rotate
//import androidx.compose.ui.layout.onSizeChanged
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.IntSize
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//import kotlin.math.sqrt
//
//
//@Composable
//fun TimerUI(
//    totalTime: Long
//) {
//    Surface(
//        color = Color(0xFF101010),
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Box(
//            contentAlignment = Alignment.Center
//        ) {
//            Timer(
//                totalTime = totalTime,
//                inactiveBarColor = Color.DarkGray,
//                activeBarColor = Color(0xFFB3B5EE),
//                modifier = Modifier.size(250.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun Timer(
//    totalTime: Long,
//    inactiveBarColor: Color,
//    activeBarColor: Color,
//    modifier: Modifier = Modifier,
//    initialValue: Float = 1f,
//    strokeWidth: Dp = 20.dp,
//) {
//    var size by remember {
//        mutableStateOf(IntSize.Zero)
//    }
//    var value by remember {
//        mutableStateOf(initialValue)
//    }
//    var currentTime by remember {
//        mutableStateOf(totalTime)
//    }
//    var isTimerRunning by remember {
//        mutableStateOf(false)
//    }
//
//    val onDecreaseClick = {
//        if (!isTimerRunning) {
//            currentTime -= 300000
//            if (currentTime < 0) currentTime = 0
//        }
//    }
//    val onIncreaseClick = {
//        if (!isTimerRunning) {
//            currentTime += 300000
//        }
//    }
//
//    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
//        if (currentTime > 0 && isTimerRunning) {
//            delay(100L)
//            currentTime -= 100L
//            value = currentTime / totalTime.toFloat()
//        }
//
//    }
//
//
//    val minutes = (currentTime % 3600000) / 60000
//    val seconds = (currentTime % 60000) / 1000
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = modifier
//            .onSizeChanged {
//                size = it
//            }
//    ) {
//        Canvas(modifier = modifier) {
//            drawArc(
//                color = inactiveBarColor,
//                startAngle = 0f,
//                sweepAngle = 360f,
//                useCenter = false,
//                size = Size(size.width.toFloat(), size.height.toFloat()),
//                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
//            )
//            drawArc(
//                color = activeBarColor,
//                startAngle = 0f,
//                sweepAngle = 360f * value,
//                useCenter = false,
//                size = Size(size.width.toFloat(), size.height.toFloat()),
//                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
//            )
//
//
////            val center = Offset(size.width / 2f, size.height / 2f)
////            val beta = (360f * value ) * (PI / 180f).toFloat()
////            val r = size.width / 2f
////            val a = cos(beta) * r
////            val b = sin(beta) * r
////            drawPoints(
////                listOf(Offset(center.x + a, center.y + b)),
////                pointMode = PointMode.Points,
////                color = handleColor,
////                strokeWidth = (strokeWidth * 3f).toPx(),
////                cap = StrokeCap.Round
////            )
//        }
//        Text(
//            text = String.format("%02d:%02d", minutes, seconds),
//            fontSize = 44.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
////        Row(verticalAlignment = Alignment.CenterVertically
////            ,modifier = Modifier.align(Alignment.Center)
////           ) {
////            Triangle(
////                modifier = Modifier,-90f,onDecreaseClick)
////
////
////
////            Triangle(
////                modifier = Modifier,90f,onIncreaseClick)
////        }
//
//        Button(
//            onClick = {
//                if (currentTime <= 0L) {
//                    currentTime = totalTime
//                    isTimerRunning = true
//                } else {
//                    isTimerRunning = !isTimerRunning
//                }
//            },
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(bottom = 32.dp),
//            colors = ButtonDefaults.buttonColors(
//                backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
//                    Color(0xFFB3B5EE)
//                } else {
//                    Color(0xFF4777AF)
//                }
//            )
//        ) {
////            Text(
////                text = if (isTimerRunning && currentTime >= 0L) "Stop"
////                else if (!isTimerRunning && currentTime >= 0L) "Start"
////                else "Restart"
////            )
////        }
//        }
//    }
//
//
//    @Composable
//    fun Triangle(modifier: Modifier, rotationAngle: Float, onClick: () -> Unit) {
//        Canvas(modifier = modifier
//            .clickable {
//                onClick()
//            }
//            .size(30.dp, 30.dp)) {
//            val buttonSize = with(LocalDensity) { 10.dp.toPx() }
//            val triangleHeight = buttonSize * sqrt(3f) / 2f
//            val offset = 10.dp.toPx()
//            drawContext.canvas.save()
//            if (rotationAngle < 0) {
//
//                drawContext.canvas.rotate(rotationAngle, center.x - offset, center.y)
//                val trianglePath = Path().apply {
//                    moveTo(center.x - offset, center.y - buttonSize / 2)
//                    lineTo(center.x - offset - triangleHeight / 2, center.y + buttonSize / 2)
//                    lineTo(center.x - offset + triangleHeight / 2, center.y + buttonSize / 2)
//                    close()
//                }
//                drawPath(
//                    path = trianglePath,
//                    color = Color.White,
//                    style = Fill,
//
//                    )
//            } else {
//                drawContext.canvas.rotate(rotationAngle, center.x + offset, center.y)
//                val trianglePath = Path().apply {
//                    moveTo(center.x + offset, center.y - buttonSize / 2)
//                    lineTo(center.x + offset - triangleHeight / 2, center.y + buttonSize / 2)
//                    lineTo(center.x + offset + triangleHeight / 2, center.y + buttonSize / 2)
//                    close()
//                }
//                drawPath(
//                    path = trianglePath,
//                    color = Color.White,
//                    style = Fill,
//                )
//            }
//            drawContext.canvas.restore()
//        }
//    }
//}
//

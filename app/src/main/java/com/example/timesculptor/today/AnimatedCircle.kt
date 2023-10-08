package com.example.timesculptor.today

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.timesculptor.util.lightBlue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timesculptor.R
import com.example.timesculptor.util.AppUtil.toHoursMinutes
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import com.example.timesculptor.util.greyOrange
import kotlin.math.abs
import kotlin.math.round





@Composable
fun AnimatedCircle(
    percentage: Float,
    timeLeft: Long,
    percentageForPickup: Float,
    fontSize: TextUnit = 14.sp,
    radius: Dp = 50.dp,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 1000
) {
    val cabinFontFamily = FontFamily(
        Font(R.font.cabin))
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    val curPercentageForPickup = animateFloatAsState(
        targetValue = if (animationPlayed) percentageForPickup else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.size(radius * 2f).padding(top = 16.dp)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = lightBlue,
                -90f,
                360 * curPercentage.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Canvas(modifier = Modifier.size((radius-10.dp) * 2f)) {
            val verticalOffset = 10.dp.toPx()
            translate(0f, verticalOffset) {
            drawArc(
                color = greyOrange,
                -90f,
                360 * curPercentageForPickup.value,
                useCenter = false,
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )}}
        Column(
            modifier = Modifier.padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text =
                if(timeLeft < 0){
                    abs(timeLeft).toHoursMinutes()
                }else{
                    timeLeft.toHoursMinutes()
                },
                color = Color.White,
                fontSize = fontSize

            )
            Text(
                text = if(timeLeft < 0 ){
                    "exceeded"
                }else{
                    "Left"},
                color = Color.White,
                fontSize = fontSize
            )
        }
    }

}












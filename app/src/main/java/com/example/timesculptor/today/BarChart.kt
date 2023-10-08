package com.example.timesculptor.today

import android.graphics.DashPathEffect
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timesculptor.util.tiffanyBlue

@Composable
fun CustomChart(
    barValue: List<Float>,
    xAxisScale: List<String>,
    total_amount: Int
) {
    val context = LocalContext.current
    // BarGraph Dimensions
    val barGraphHeight by remember { mutableStateOf(140.dp) }
    val barGraphWidth by remember { mutableStateOf(6.dp) }
    // Scale Dimensions
    val scaleYAxisWidth by remember { mutableStateOf(30.dp) }
    val scaleLineWidth by remember { mutableStateOf(2.dp) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        Box( modifier = Modifier
            .height(1.dp)
            .padding(start = 16.dp)
            .padding(top = 26.dp),) {
            DashedDivider()
        }

        Box( modifier = Modifier
            .height(1.dp)
            .padding(start = 16.dp)
            .padding(top = 82.dp),) {
            DashedDivider()
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(barGraphHeight),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            // scale Y-Axis
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleYAxisWidth),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp)
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = total_amount.toString(),
                        color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.fillMaxHeight())
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp)
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = (total_amount / 2).toString(),
                        color = Color.White, fontSize = 12.sp)
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                }

            }

            // Y-Axis Line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(scaleLineWidth)
                    .background(Color.Transparent)
            )

            // graph
            barValue.forEach {
                Box(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .padding(bottom = 4.dp)
                        .offset(y = 4.dp)
                        .clip(CircleShape)
                        .width(barGraphWidth)
                        .fillMaxHeight(it)
                        .background(tiffanyBlue)
                        .clickable {
                            Toast
                                .makeText(
                                    context,
                                    "${(it * 60f).toInt()} minutes",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                )
            }

        }




        // X-Axis Line
        Box(
            modifier = Modifier
                .padding(start = scaleYAxisWidth)
                .padding(end = 24.dp)
                .padding(bottom = 4.dp)
                .fillMaxWidth()
                .height(scaleLineWidth)
                .background(Color.White)
        )

        // Scale X-Axis
        Row(
            modifier = Modifier
                .padding(start = scaleYAxisWidth  + scaleLineWidth)
                .padding(end = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(barGraphWidth)
        ) {

            xAxisScale.forEachIndexed { index, label ->
                Box(modifier = Modifier.wrapContentWidth()) {
                    if (index % 6 == 0) {
                        Text(
                            color = Color.White,
                            fontSize = 10.sp,
                            text = label,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DashedDivider() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {

        drawLine(
            color = Color.White,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}



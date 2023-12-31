package com.example.timesculptor.homepage


import android.graphics.drawable.Drawable
import android.view.View
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.timesculptor.R
import com.example.timesculptor.util.AppUtil.toHoursMinutesSeconds
import com.example.timesculptor.util.azureBlue
import com.example.timesculptor.util.blushPetal
import com.example.timesculptor.util.coralBlush
import com.example.timesculptor.util.darkBlue
import com.example.timesculptor.util.darkGreen
import com.example.timesculptor.util.fadedWalnut
import com.example.timesculptor.util.forestShade
import com.example.timesculptor.util.goldenSunray
import com.example.timesculptor.util.greyRed
import com.example.timesculptor.util.lavenderMist
import com.example.timesculptor.util.lightGreen
import com.example.timesculptor.util.mistyHarbor
import com.example.timesculptor.util.pink
import com.example.timesculptor.util.sand
import com.example.timesculptor.util.softLilac
import com.example.timesculptor.util.softLinen
import com.example.timesculptor.util.springMeadow
import com.example.timesculptor.util.tiffanyBlue
import com.google.accompanist.drawablepainter.rememberDrawablePainter


@Composable
fun PieChart(
    data: Map<String, Long>,
    icon: List<Drawable>,
    packageName: List<String>,
    onClick: (String) -> Unit,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 20.dp,
    animDuration: Int = 1000,
) {
    val totalSum = data.values.sum()
    val floatValue = mutableListOf<Float>()

    // To set the value of each Arc according to
    // the value given in the data, we have used a simple formula.
    data.values.forEachIndexed { index, values ->
        floatValue.add(index, 360 * values.toFloat() / totalSum.toFloat())
    }

    // add the colors as per the number of data(no. of pie chart entries)
    // so that each data will get a color
    val colors = listOf(
        tiffanyBlue,
        sand,
        pink,
        greyRed,
        darkBlue,
        lightGreen,
        fadedWalnut,
//        goldenSunray,
//        forestShade,
//        softLinen

    )

    var animationPlayed by remember { mutableStateOf(false) }

    var lastValue = 0f

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier.size(animateSize.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                floatValue.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Round)
                    )
                    lastValue += value
                }
            }
        }

        // To see the data in more structured way
        // Compose Function in which Items are showing data
        DetailsPieChart(
            data = data,
            colors = colors,
            icon = icon,
            packageName = packageName,
            onClick = onClick
        )

    }

}

@Composable
fun DetailsPieChart(
    data: Map<String, Long>,
    icon: List<Drawable>,
    packageName: List<String>,
    colors: List<Color>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth()
    ) {
        // create the data items
        data.values.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = Pair(data.keys.elementAt(index), value),
                color = colors[index],
                icon = icon[index],
                packageName = packageName[index],
                onClick = onClick
            )
        }

    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, Long>,
    icon: Drawable,
    packageName: String,
    color: Color,
    onClick: (String) -> Unit
) {


    Surface(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp)
            .clickable {
                onClick(packageName)
            },
        color = Color.Transparent
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = color,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .size(width = 10.dp, height = 40.dp)
            )
            Image(
                painter = rememberDrawablePainter(icon),
                contentDescription = data.first,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 40.dp, height = 40.dp)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = data.first,
                    fontWeight = FontWeight.Medium,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.second.toHoursMinutesSeconds(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

        }

    }
}



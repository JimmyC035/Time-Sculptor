package com.example.timesculptor.today

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.timesculptor.util.backgroundColor
import com.example.timesculptor.util.darkBlue


@Composable
fun LineChartUI(pointsData: List<Point>, steps: Int) {
//    val steps = 5
//    val pointsData = listOf(
//        Point(0f,40f) ,
//        Point(1f,90f),
//        Point(2f,0f),
//        Point(3f,60f),
//        Point(4f,10f),
//
//    )

    val xAxisData = AxisData.Builder()
        .axisStepSize(15.dp)
        .backgroundColor(backgroundColor)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .axisLabelFontSize(10.sp)
        .labelAndAxisLinePadding(15.dp)
        .axisLineColor(Color.Red)
        .axisLabelColor(Color.Red)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(backgroundColor)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = 60 / steps
            (i * yScale).toString()
        }
        .axisLabelColor(Color.Green)
        .axisLineColor(Color.Green)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = darkBlue,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = darkBlue,
                        radius = 1.dp
                    ),
                    SelectionHighlightPoint(
                        color = darkBlue
                    ),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                darkBlue,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            enableHorizontalLines = false,
            enableVerticalLines = false
        ),
        backgroundColor = backgroundColor,
        paddingRight = 0.dp
    )


    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}
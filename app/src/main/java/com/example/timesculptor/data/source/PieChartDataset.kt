package com.example.timesculptor.data.source

import android.graphics.drawable.Drawable

data class PieChartDataset(
    val dataMap: MutableMap<String,Long> = mutableMapOf(),
    val icons: MutableList<Drawable> = mutableListOf<Drawable>(),
    var packageNames : MutableList<String> = mutableListOf<String>()
)
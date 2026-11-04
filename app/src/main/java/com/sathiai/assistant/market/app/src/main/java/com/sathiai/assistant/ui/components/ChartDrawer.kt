package com.sathiai.assistant.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SimpleLineChart(
    dataPoints: List<Float>,
    lineColor: Color = Color(0xFF80CBC4)
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (dataPoints.isEmpty()) return@Canvas

        val maxVal = dataPoints.maxOrNull() ?: 1f
        val minVal = dataPoints.minOrNull() ?: 0f
        val range = maxVal - minVal
        val stepX = size.width / (dataPoints.size - 1)

        val path = Path()

        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            // Y অক্ষকে উল্টে দিতে হবে কারণ ক্যানভাসে (0,0) উপরে বাম দিকে থাকে
            val normalizedY = if (range == 0f) size.height / 2 else (value - minVal) / range
            val y = size.height - (normalizedY * size.height)

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 4f)
        )
    }
}

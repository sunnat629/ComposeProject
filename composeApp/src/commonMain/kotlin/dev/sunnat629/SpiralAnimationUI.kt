/**
 * Inspired by https://x.com/_take_hito_/status/1873978363444072731
 **/

package dev.sunnat629

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

val currentTime: Long get() = Clock.System.now().toEpochMilliseconds()

@Composable
fun SpiralAnimationUI(modifier: Modifier = Modifier) {
    // Holds the start time for the animation
    var start by remember { mutableStateOf(currentTime) }
    // Tracks the size of the canvas for centering the spiral
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val timeMs by remember { mutableStateOf(10L) }

    // LaunchedEffect to continuously update the start time for animation
    LaunchedEffect(Unit) {
        while (true) {
            delay(timeMs)
            start += timeMs
        }
    }

    // Canvas for custom drawing
    Canvas(
        modifier = modifier
            .background(Color.Black)
            .onSizeChanged { canvasSize = Size(it.width.toFloat(), it.height.toFloat()) }
            .pointerInput(Unit) {
                // Reset animation on tap
                detectTapGestures { start = currentTime }
            }
    ) {
        // Calculate elapsed time and rotation factor
        val time: Double = (currentTime - start) / 120.0
        val radius = 2.0
val rotation = 0.7 + 0.3 * abs(sin(PI * time / 2) + 0.5)
        // Calculate the center point for the spiral
        val center = Offset(canvasSize.width / 2 - radius.toFloat(), canvasSize.height / 2 - radius.toFloat())

        // Loop to draw multiple points in the spiral pattern
        for (i in 0 until 3000) {
            val j = (i + time) * 0.1f
//            val j = rotation * i // Rotate each point
            val p = Offset.spiralAt(j) / .5F // Calculate spiral position
            val pathOffset = Offset(center.x + p.x.toFloat(), center.y + p.y.toFloat())
            drawCircle(
                color = Color.hsv(((j % 255 / 255f) * 360f).toFloat(), 0.6f, 1f),
                radius = radius.toFloat(),
                center = pathOffset
            )
        }
    }
}

fun Offset.Companion.spiralAt(angle: Double): Offset {
    return Offset((angle * cos(angle).toFloat()).toFloat(), (angle * sin(angle).toFloat()).toFloat())
}
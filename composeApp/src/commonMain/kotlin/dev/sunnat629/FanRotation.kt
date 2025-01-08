package dev.sunnat629

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import composeproject.composeapp.generated.resources.Res
import composeproject.composeapp.generated.resources.ic_fan
import composeproject.composeapp.generated.resources.ic_sunrise
import org.jetbrains.compose.resources.painterResource

@Composable
fun FanControlUI() {
    var isRunning by remember { mutableStateOf(false) }
    var rotationSpeed by remember { mutableStateOf(500L) }
    val rotationAngle = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Fan Image Rotation Animation
    LaunchedEffect(isRunning, rotationSpeed) {
        while (isRunning) {
            rotationAngle.animateTo(
                targetValue = rotationAngle.value + 360f,
                animationSpec = tween(durationMillis = rotationSpeed.toInt(), easing = LinearEasing)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Fan Image
        Icon(
            painter = painterResource(Res.drawable.ic_sunrise),
            contentDescription = null
        )

        Image(
            painter = painterResource(Res.drawable.ic_fan),
            contentDescription = "Fan",
            colorFilter = ColorFilter.tint(Color.Cyan),
            modifier = Modifier
                .size(150.dp)
                .graphicsLayer(rotationZ = rotationAngle.value)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Control Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (rotationSpeed > 100L) {
                    rotationSpeed -= 100L
                }
            }) {
                Text("Speed Up")
            }

            Button(onClick = {
                isRunning = !isRunning
            }) {
                Text(if (isRunning) "Stop" else "Start")
            }

            Button(onClick = {
                if (rotationSpeed < 2000L) {
                    rotationSpeed += 100L
                }
            }) {
                Text("Speed Down")
            }

        }
    }
}
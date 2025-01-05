import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composeproject.composeapp.generated.resources.Res
import composeproject.composeapp.generated.resources.ic_sunrise
import dev.sunnat629.Greeting
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunriseNSunsetUI() {
    Column {
        val greeting = remember { Greeting().greet() }
        Text("Compose: $greeting")

              Box(
            modifier = Modifier.fillMaxSize().background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
                 CircularSunriseSunset()
        }
    }
}

@Composable
fun CircularSunriseSunset() {
    val sunrise = LocalTime(hour = 7, minute = 30)
    val sunset = LocalTime(hour = 16, minute = 0)
    val currentTime = remember { mutableStateOf(LocalTime(hour = 14, minute = 30, second = 0)) }

            val dayColor = remember { Color(0xFFFFA726) }
            val nightColor = remember { Color(0xFF90CAF9) }

    //
    //    LaunchedEffect(Unit) {
    //        while (true) {
    //            delay(10L)
    //            val newMinute = (currentTime.value.minute + 1) % 60
    //            val newHour = (currentTime.value.hour + (if (newMinute == 0) 1 else 0)) % 24
    //            currentTime.value = LocalTime(hour = newHour, minute = newMinute, second = 0)
    //        }
    //    }

    //    val currentTime = remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10L)
            val newMinute = (currentTime.value.minute + 1) % 60
            val newHour = (currentTime.value.hour + (if (newMinute == 0) 1 else 0)) % 24
            currentTime.value = LocalTime(hour = newHour, minute = newMinute, second = 0)
            //            currentTime.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
        }
    }

    val sunriseMinutes = sunrise.hour * 60 + sunrise.minute
    val sunsetMinutes = sunset.hour * 60 + sunset.minute
    val currentMinutes = currentTime.value.hour * 60 + currentTime.value.minute

    val dayDuration = sunsetMinutes - sunriseMinutes
    val nightDuration = (24 * 60) - dayDuration

    val progress = when {
        currentMinutes in sunriseMinutes until sunsetMinutes -> (currentMinutes - sunriseMinutes) / dayDuration.toFloat()
        currentMinutes < sunriseMinutes -> (currentMinutes + (24 * 60) - sunsetMinutes) / nightDuration.toFloat()
        else -> (currentMinutes - sunsetMinutes) / nightDuration.toFloat()
    }

    val isDay = currentMinutes in sunriseMinutes until sunsetMinutes
    val painter = rememberVectorPainter(if (isDay) Icons.Default.WbSunny else Icons.Default.Nightlight)

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(300.dp)) {

            drawCircle(
                color = Color.LightGray.copy(alpha = 0.3f),
                radius = size.minDimension / 2
            )

            val radius = size.minDimension / 2
            val centerOffset = Offset(size.width / 2, size.height / 2)

            val sweepAngle = progress * 180
            val startAngle = if (isDay) 180f - sweepAngle else sweepAngle

            val radian = if (isDay) (-startAngle) * (PI / 180).toFloat() else (startAngle) * (PI / 180).toFloat()
            val iconOffset = Offset(
                x = centerOffset.x + radius * cos(radian),
                y = centerOffset.y + radius * sin(radian)
            )

            drawArc(
                color = Color.DarkGray.copy(alpha = 0.4f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(12f)
            )

            drawArc(
                color = if (isDay) dayColor else nightColor,
                startAngle = if (isDay) 180f else 0f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(12f)
            )

            with(drawContext.canvas) {
                val bgSize = 60f

                translate(iconOffset.x - bgSize / 2, iconOffset.y - bgSize / 2) {
                    drawRoundRect(
                        color = Color.White,
                        size = androidx.compose.ui.geometry.Size(bgSize, bgSize),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(30f, 30f)
                    )
                }
            }

            with(painter) {
                val iconSize = 28f

                translate(iconOffset.x - iconSize / 2, iconOffset.y - iconSize / 2) {
                    draw(
                        size = Size(iconSize, iconSize),
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(if (isDay) dayColor else nightColor)
                    )
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(painter = painterResource(Res.drawable.ic_sunrise), contentDescription = null, tint = nightColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text("Sunset", fontSize = 16.sp, color = Color.Gray)
        Text("${sunset.hour.toString().padStart(2, '0')}:${sunset.minute.toString().padStart(2, '0')}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("${currentTime.value}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Sunrise", fontSize = 16.sp, color = Color.Gray)
        Text("${sunrise.hour.toString().padStart(2, '0')}:${sunrise.minute.toString().padStart(2, '0')}", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Icon(imageVector = Icons.Default.WbTwilight, contentDescription = null, tint = dayColor)
    }
}


@Preview
@Composable
fun PreviewSunriseSunsetUI() {
    MaterialTheme {
        SunriseNSunsetUI()
    }
}
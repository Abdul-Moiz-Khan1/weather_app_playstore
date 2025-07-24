package moiz.dev.android.weatherApp.presentation.view

import android.graphics.SweepGradient
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toAndroidRectF
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.data.model.DailyForecastItem
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import moiz.dev.android.weatherApp.ui.theme.grad_home_above
import moiz.dev.android.weatherApp.ui.theme.grad_home_below
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_bottom
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_top
import moiz.dev.android.weatherApp.utils.Utils
import moiz.dev.android.weatherApp.utils.Utils.ShowLoading
import moiz.dev.android.weatherApp.utils.Utils.getDailyForecastItems
import moiz.dev.android.weatherApp.utils.Utils.getDayOfWeek
import moiz.dev.android.weatherApp.utils.Utils.getLocationName
import moiz.dev.android.weatherApp.utils.Utils.tempToInt

@Composable
fun Details(navController: NavController , viewModel: WeatherViewModel) {
//    viewModel.loadCacheData()

//    viewModel.loadCacheData()
    val forecast = viewModel.forecast.observeAsState()
    Log.d("forecast" , forecast.value.toString())

    if (forecast.value != null) {
        ShowData(navController, forecast.value!!)
    } else {
        ShowLoading()
    }


}

@Composable
fun ShowData(navController: NavController, forecast: ApiResponse) {


    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        grad_home_below,
                        grad_home_above,
                        grad_home_above,
                    )
                )
            )
            .padding(16.dp),
    ) {
        ShowBasics(navController, forecast)
        Spacer(modifier = Modifier.height(8.dp))
        DetailedWeeklyForecastCard(forecast)
        Spacer(modifier = Modifier.height(16.dp))
        ShowUvStatus(forecast.currentConditions.uvindex)
    }
}

@Composable
fun ShowBasics(navController: NavController, forecast: ApiResponse) {
    val address = forecast?.address
    var locationName by remember { mutableStateOf<String?>(address) }
    val context = LocalContext.current
    LaunchedEffect(address) {
        if (!forecast?.address.isNullOrEmpty() && address.contains(",")) {
            val parts = address.split(",")
            if (parts.size == 2) {
                val lat = parts[0].trim().toDoubleOrNull()
                val lon = parts[1].trim().toDoubleOrNull()
                if (lat != null && lon != null) {
                    locationName = getLocationName(context, lat, lon)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(16.dp), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(R.drawable.next),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clickable { navController.popBackStack() }
                    .rotate(180f)
            )
        }
        Text(
            text = locationName ?: address ?: "Unknown location",
            color = Color.White,
            fontSize = 26.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "${tempToInt(forecast.currentConditions.temp)}°",
            color = Color.White,
            fontSize = 60.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal
        )
        Text(
            "${forecast.currentConditions.conditions}°",
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "H:",
                color = Color.Gray,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${tempToInt(forecast.days[0].tempmax)}°  ",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(
                "L:",
                color = Color.Gray,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
            Text(
                "${tempToInt(forecast.days[0].tempmin)}°",
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DetailedWeeklyForecastCard(forcast: ApiResponse?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        main_card_grad_top,
                        main_card_grad_bottom,
                        main_card_grad_bottom,
                    )
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text("High  |  Low", color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(8.dp))
        for (i in 0..6) {
            DetailedWeeklyForecastView(
                forcast,
                i,
            )
        }

    }
}

@Composable
fun DetailedWeeklyForecastView(forcast: ApiResponse?, i: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        var isRotated by remember { mutableStateOf(false) }
        val rotationAngle by animateFloatAsState(
            targetValue = if (isRotated) 270f else 90f,
            animationSpec = tween(durationMillis = 300)
        )
        Image(
            painter = painterResource(R.drawable.next),
            modifier = Modifier
                .rotate(rotationAngle)
                .clickable {
                    isRotated = !isRotated
                },
            contentDescription = null
        )
        if (isRotated) {
            ShowHourlyData(forcast , i)
        }
        Text(text = getDayOfWeek(forcast?.days[i]?.datetime.toString()), color = Color.White)
        Image(
            painter = painterResource(id = Utils.getImage(forcast?.days[i]?.conditions.toString())),
            contentDescription = "null",
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = "${
                tempToInt(
                    forcast?.days[i]?.tempmax.toString().toDouble()
                )
            }°C", color = Color.White
        )
        Text(
            text = "${
                tempToInt(
                    forcast?.days[i]?.tempmin.toString().toDouble()
                )
            }°C", color = Color.White
        )
    }
}

@Composable
fun ShowUvStatus(uvIndex: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        main_card_grad_top,
                        main_card_grad_bottom,
                        main_card_grad_bottom,
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
            text = "UV Index",
            textAlign = TextAlign.Center,
            fontSize = 22.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            text = getUvDescription(uvIndex),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal
        )
        UVIndexIndicator(uvIndex)

    }

}

@Composable
fun UVIndexIndicator(
    uvIndex: Int,
    modifier: Modifier = Modifier.size(200.dp)
) {
    val sweepAngle = (uvIndex.coerceIn(0, 11) / 11f) * 170f
    val level = getUvLevel(uvIndex)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background track (gray)
//            drawArc(
//                color = Color.Transparent,
//                startAngle = 135f,
//                sweepAngle = 270f,
//                useCenter = false,
//                style = Stroke(width = 20f, cap = StrokeCap.Round)
//            )

            // Gradient progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Green, Color.Yellow, Color.Red),
                    center = center
                ),
                startAngle = 185f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = uvIndex.toString(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = level,
                fontSize = 16.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ShowHourlyData(forecast: ApiResponse? , index:Int) {
    val list = getDailyForecastItems(forecast?.days?.get(index))
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -20 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -20 })
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(getDayOfWeek(forecast?.days?.get(index)?.datetime.toString()), fontSize = 20.sp, color = Color.White)
            ScrollableData(list)
        }

    }
}

@Composable
fun ScrollableData(list: List<DailyForecastItem>) {
    LazyRow(
        modifier = Modifier
            .padding(top = 16.dp, start = 8.dp)
    ) {
        items(list) { item: DailyForecastItem ->
            DailyForecastView(
                item, modifier = Modifier
                    .height(120.dp)
                    .width(70.dp), 35
            )
        }
    }
}

fun getUvDescription(index: Int): String {
    return when (index) {
        in 0..2 -> "Low risk. Safe for most outdoor activities."
        in 3..5 -> "Moderate risk. Use sunscreen and wear a hat."
        in 6..7 -> "High risk. Use sunscreen, sunglasses, and limit sun exposure."
        in 8..10 -> "Very high risk. Seek shade and avoid direct sun during peak hours."
        else -> "Extreme risk. Stay indoors or cover up completely."
    }
}

fun getUvLevel(index: Int): String {
    return when (index) {
        in 0..2 -> "Low"
        in 3..5 -> "Moderate"
        in 6..7 -> "High"
        in 8..10 -> "Very High"
        else -> "Extreme"
    }
}



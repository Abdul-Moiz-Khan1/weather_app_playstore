package moiz.dev.android.weatherApp.presentation.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.data.model.response.Weather
import moiz.dev.android.weatherApp.ui.theme.cards_bg
import moiz.dev.android.weatherApp.ui.theme.grad_home_above
import moiz.dev.android.weatherApp.ui.theme.grad_home_below
import moiz.dev.android.weatherApp.ui.theme.text_left_grad
import moiz.dev.android.weatherApp.ui.theme.text_right_grad

@Composable
fun Home(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    viewModel.loadCacheData()
    val forcast = viewModel.forecast.observeAsState()
    Log.d("CatchError_in_home", forcast.toString())
    if (forcast.value == null) {
        showLoading()
    } else {
        ShowUi(forcast.value)
    }

}


@Composable
fun showLoading(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.weather_loading2))
    val progress by animateLottieCompositionAsState(composition, iterations = Int.MAX_VALUE)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        grad_home_above,
                        grad_home_above,
                        grad_home_below
                    )
                )
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition, progress)
    }

}

@Composable
fun ShowUi(forcast: Weather?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        grad_home_above,
                        grad_home_above,
                        grad_home_below
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(

            "${forcast?.current?.is_day}, ${forcast?.forecast?.forecastday[0]?.date}",
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(cards_bg)
                .padding(12.dp), textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(getImage(forcast?.current?.condition?.text.toString())),
                    contentDescription = "null",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.6f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Feels like ${forcast?.current?.feelslike_c}°C",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "${forcast?.current?.temp_c}°",
                    fontSize = 60.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                text_left_grad,
                                text_right_grad
                            )
                        )
                    )
                )
                Text(
                    "${forcast?.current?.condition?.text}",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Wind ${forcast?.current?.wind_kph} Km/h",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Light
                )

            }
        }
        Divider(
            color = text_right_grad,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                attribute(
                    R.drawable.precipitation,
                    "Precipitation",
                    "${forcast?.current?.precip_mm} mm"
                )
                attribute(R.drawable.humidity, "Humidity", "${forcast?.current?.humidity}%")

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                attribute(R.drawable.wind, "Wind", "${forcast?.current?.wind_kph} Km/h")
                attribute(
                    R.drawable.sunset,
                    "Sunset",
                    "${forcast?.forecast?.forecastday[0]?.astro?.sunset}"
                )

            }
        }
    }
}


@Composable
fun attribute(image: Int, attribute: String, value: String) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "null",
            modifier = Modifier.size(40.dp)
        )
        Text("${attribute}:", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Light)
        Text(value, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}


fun getImage(condition: String): Int {
    return when {
        condition.contains("sunny", ignoreCase = true) -> R.drawable.sunny
        condition.contains("clear", ignoreCase = true) -> R.drawable.splash_img
        condition.contains("cloudy", ignoreCase = true) -> R.drawable.partly_cloudy
        condition.contains("rain", ignoreCase = true) -> R.drawable.rainy
        condition.contains("wind", ignoreCase = true) -> R.drawable.windy_cloudy
        else -> R.drawable.splash_img
    }
}
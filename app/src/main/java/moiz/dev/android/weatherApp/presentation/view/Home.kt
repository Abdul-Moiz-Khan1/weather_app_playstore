package moiz.dev.android.weatherApp.presentation.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.Utils.getDayOfWeek
import moiz.dev.android.weatherApp.data.model.DailyForecastItem
import moiz.dev.android.weatherApp.data.model.response.Weather
import moiz.dev.android.weatherApp.ui.theme.cards_bg
import moiz.dev.android.weatherApp.ui.theme.grad_home_above
import moiz.dev.android.weatherApp.ui.theme.grad_home_below
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_bottom
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_top
import moiz.dev.android.weatherApp.ui.theme.text_left_grad
import moiz.dev.android.weatherApp.ui.theme.text_right_grad

@Composable
fun Home(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val context = LocalContext.current
    val forcast = viewModel.forecast.observeAsState()
    Log.d("CatchError_in_home", forcast.value.toString())
    Log.d("CatchError_size", forcast.value?.)
    if (forcast.value == null) {
//        showLoading()
    } else {
//        ShowUi(forcast.value)
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
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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

        val dailyForecastList = listOf<DailyForecastItem>(
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(0)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(0)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(0)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(1)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(1)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(1)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(2)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(2)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(2)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(3)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(3)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(3)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(4)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(4)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(4)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(5)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(5)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(5)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(6)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(6)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(6)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(7)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(7)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(7)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(8)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(8)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(8)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(9)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(9)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(9)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(10)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(10)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(10)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(11)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(11)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(11)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(12)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(12)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(12)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(13)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(13)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(13)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(14)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(14)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(14)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(15)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(15)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(15)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(16)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(16)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(16)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(17)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(17)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(17)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(18)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(18)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(18)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(19)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(19)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(19)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(20)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(20)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(20)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(21)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(21)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(21)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(22)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(22)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(22)?.temp_c.toString(),
            ),
            DailyForecastItem(
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(23)?.time.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(23)?.condition?.icon.toString(),
                forcast?.forecast?.forecastday?.get(0)?.hour?.get(23)?.temp_c.toString(),
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(

            "${forcast?.location?.name}",
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
                    .fillMaxHeight(0.12f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(getImage(forcast?.current?.condition?.text.toString())),
                    contentDescription = "null",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentScale = ContentScale.Crop
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
                    .fillMaxHeight(0.12f),
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
        custom_divider()

        Spacer(modifier = Modifier.height(8.dp))
        Column(//attributes
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                attribute(
                    R.drawable.precipitation,
                    "Precipitation",
                    "${forcast?.current?.precip_mm} mm"
                )
                attribute(R.drawable.wind, "Wind", "${forcast?.current?.wind_kph} Km/h")


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                attribute(R.drawable.humidity, "Humidity", "${forcast?.current?.humidity}%")

                attribute(
                    R.drawable.sunset,
                    "Sunset",
                    "${forcast?.forecast?.forecastday[0]?.astro?.sunset}"
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        custom_divider()
        ScrollableRow(dailyForecastList)
        Spacer(modifier = Modifier.height(8.dp))
        WeeklyForecastCard(forcast)

    }

}

@Composable
fun WeeklyForecastCard(forcast: Weather?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(1f)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        main_card_grad_top,
                        main_card_grad_bottom
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

        WeeklyForecastView(
            forcast?.forecast?.forecastday[0]?.date.toString(),
            forcast?.forecast?.forecastday[0]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[0]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[0]?.day?.mintemp_c.toString(),
        )
        WeeklyForecastView(
            forcast?.forecast?.forecastday[1]?.date.toString(),
            forcast?.forecast?.forecastday[1]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[1]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[1]?.day?.mintemp_c.toString(),
        )
        WeeklyForecastView(
            forcast?.forecast?.forecastday[2]?.date.toString(),
            forcast?.forecast?.forecastday[2]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[2]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[2]?.day?.mintemp_c.toString(),
        )
        WeeklyForecastView(
            forcast?.forecast?.forecastday[0]?.date.toString(),
            forcast?.forecast?.forecastday[0]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[0]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[0]?.day?.mintemp_c.toString(),
        )
        WeeklyForecastView(
            forcast?.forecast?.forecastday[1]?.date.toString(),
            forcast?.forecast?.forecastday[1]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[1]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[1]?.day?.mintemp_c.toString(),
        )
        WeeklyForecastView(
            forcast?.forecast?.forecastday[2]?.date.toString(),
            forcast?.forecast?.forecastday[2]?.day?.condition?.text.toString(),
            forcast?.forecast?.forecastday[2]?.day?.maxtemp_c.toString(),
            forcast?.forecast?.forecastday[2]?.day?.mintemp_c.toString(),
        )
//        WeeklyForecastView(
//            forcast?.forecast?.forecastday[3]?.date.toString(),
//            forcast?.forecast?.forecastday[3]?.day?.condition?.text.toString(),
//            forcast?.forecast?.forecastday[3]?.day?.maxtemp_c.toString(),
//            forcast?.forecast?.forecastday[3]?.day?.mintemp_c.toString(),
//        )
//        WeeklyForecastView(
//            forcast?.forecast?.forecastday[4]?.date.toString(),
//            forcast?.forecast?.forecastday[4]?.day?.condition?.text.toString(),
//            forcast?.forecast?.forecastday[4]?.day?.maxtemp_c.toString(),
//            forcast?.forecast?.forecastday[4]?.day?.mintemp_c.toString(),
//        )
//        WeeklyForecastView(
//            forcast?.forecast?.forecastday[5]?.date.toString(),
//            forcast?.forecast?.forecastday[5]?.day?.condition?.text.toString(),
//            forcast?.forecast?.forecastday[5]?.day?.maxtemp_c.toString(),
//            forcast?.forecast?.forecastday[5]?.day?.mintemp_c.toString(),
//        )
//        WeeklyForecastView(
//            forcast?.forecast?.forecastday[6]?.date.toString(),
//            forcast?.forecast?.forecastday[6]?.day?.condition?.text.toString(),
//            forcast?.forecast?.forecastday[6]?.day?.maxtemp_c.toString(),
//            forcast?.forecast?.forecastday[6]?.day?.mintemp_c.toString(),
//        )


    }
}

@Composable
fun ScrollableRow(list: List<DailyForecastItem>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(list) { item: DailyForecastItem ->
            dailyForecastView(item)
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

@Composable
fun dailyForecastView(item: DailyForecastItem) {
    Box(
        modifier = Modifier
            .height(160.dp)
            .width(100.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .height(160.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.05f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            Color.White.copy(alpha = 0.4f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .blur(16.dp)
                .padding(12.dp)
        ) { }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                text = item.time.takeLast(5),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data("https:${item.img}")
                        .error(R.drawable.sunny)
                        .placeholder(R.drawable.sunny)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Item Image",
                modifier = Modifier
                    .size(70.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                text = item.temp,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}

@Composable
fun WeeklyForecastView(date: String, img: String, temp_high: String, temp_low: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = getDayOfWeek(date), color = Color.White)
        Image(
            painter = painterResource(id = getImage(img)),
            contentDescription = "null",
            modifier = Modifier.size(60.dp)
        )
        Text(text = "${temp_high}°C", color = Color.White)
        Text(text = "${temp_low}°C", color = Color.White)
    }
}

@Composable
fun DetailsCard(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        main_card_grad_top,
                        main_card_grad_bottom
                    )
                )
            )
            .padding(16.dp)
    ) {}
    
}

@Composable
fun custom_divider() {
    Divider(
        color = text_right_grad,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
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

//@Preview(showBackground = true)
//@Composable
//fun preview(modifier: Modifier = Modifier) {
//    dailyForecastView()
//}
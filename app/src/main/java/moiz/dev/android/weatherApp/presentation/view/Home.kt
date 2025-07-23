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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import moiz.dev.android.weatherApp.utils.Utils.getDayOfWeek
import moiz.dev.android.weatherApp.data.model.DailyForecastItem
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse
import moiz.dev.android.weatherApp.ui.theme.cards_bg
import moiz.dev.android.weatherApp.ui.theme.grad_home_above
import moiz.dev.android.weatherApp.ui.theme.grad_home_below
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_bottom
import moiz.dev.android.weatherApp.ui.theme.main_card_grad_top
import moiz.dev.android.weatherApp.ui.theme.text_left_grad
import moiz.dev.android.weatherApp.ui.theme.text_right_grad
import moiz.dev.android.weatherApp.utils.Utils

@Composable
fun Home(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    viewModel.loadCacheData()
    val forcast = viewModel.forecast.observeAsState()
    Log.d("CatchError_in_home", forcast.value.toString())
    Log.d("CatchError_size", forcast?.value?.days?.size.toString())
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
fun ShowUi(forcast: ApiResponse?) {
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
                forcast?.days?.get(0)?.hours[0]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[0]?.icon.toString(),
                forcast?.days?.get(0)?.hours[0]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[1]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[1]?.icon.toString(),
                forcast?.days?.get(0)?.hours[1]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[2]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[2]?.icon.toString(),
                forcast?.days?.get(0)?.hours[2]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[3]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[3]?.icon.toString(),
                forcast?.days?.get(0)?.hours[3]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[4]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[4]?.icon.toString(),
                forcast?.days?.get(0)?.hours[4]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[5]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[5]?.icon.toString(),
                forcast?.days?.get(0)?.hours[5]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[6]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[6]?.icon.toString(),
                forcast?.days?.get(0)?.hours[6]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[7]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[7]?.icon.toString(),
                forcast?.days?.get(0)?.hours[7]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[8]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[8]?.icon.toString(),
                forcast?.days?.get(0)?.hours[8]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[9]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[9]?.icon.toString(),
                forcast?.days?.get(0)?.hours[9]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[10]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[10]?.icon.toString(),
                forcast?.days?.get(0)?.hours[10]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[11]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[12]?.icon.toString(),
                forcast?.days?.get(0)?.hours[12]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[12]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[12]?.icon.toString(),
                forcast?.days?.get(0)?.hours[12]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[13]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[13]?.icon.toString(),
                forcast?.days?.get(0)?.hours[13]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[14]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[14]?.icon.toString(),
                forcast?.days?.get(0)?.hours[14]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[15]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[15]?.icon.toString(),
                forcast?.days?.get(0)?.hours[15]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[16]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[16]?.icon.toString(),
                forcast?.days?.get(0)?.hours[16]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[17]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[17]?.icon.toString(),
                forcast?.days?.get(0)?.hours[17]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[18]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[18]?.icon.toString(),
                forcast?.days?.get(0)?.hours[18]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[19]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[19]?.icon.toString(),
                forcast?.days?.get(0)?.hours[19]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[20]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[20]?.icon.toString(),
                forcast?.days?.get(0)?.hours[20]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[21]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[21]?.icon.toString(),
                forcast?.days?.get(0)?.hours[21]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[22]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[22]?.icon.toString(),
                forcast?.days?.get(0)?.hours[22]?.temp.toString(),

                ),
            DailyForecastItem(
                forcast?.days?.get(0)?.hours[23]?.datetime.toString(),
                forcast?.days?.get(0)?.hours[23]?.icon.toString(),
                forcast?.days?.get(0)?.hours[23]?.temp.toString(),
            ),

            )
        Spacer(modifier = Modifier.height(8.dp))
        Text(

            "${forcast?.address}",
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
                    painter = painterResource(Utils.getImage(forcast?.currentConditions?.conditions.toString())),
                    contentDescription = "null",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Feels like ${forcast?.currentConditions?.feelslike}°C",
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
                    "${forcast?.currentConditions?.temp}°",
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
                    "${forcast?.currentConditions?.conditions}",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "Wind ${forcast?.currentConditions?.windspeed} Km/h",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Light
                )

            }
        }
        Custom_divider()

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
                    "${forcast?.currentConditions?.precip} mm"
                )
                attribute(R.drawable.wind, "Wind", "${forcast?.currentConditions?.windspeed} Km/h")


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                attribute(
                    R.drawable.humidity,
                    "Humidity",
                    "${forcast?.currentConditions?.humidity}%"
                )

                attribute(
                    R.drawable.sunset,
                    "Sunset",
                    "${forcast?.currentConditions?.sunset}"
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Custom_divider()
        ScrollableRow(dailyForecastList)
        Spacer(modifier = Modifier.height(8.dp))
        WeeklyForecastCard(forcast)
        Spacer(modifier = Modifier.height(8.dp))
        DetailsCard(forcast)

    }

}

@Composable
fun WeeklyForecastCard(forcast: ApiResponse?) {
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

        for (i in 0..6) {
            WeeklyForecastView(
                forcast?.days[i]?.datetime.toString(),
                forcast?.days[i]?.conditions.toString(),
                forcast?.days[i]?.tempmax.toString(),
                forcast?.days[i]?.tempmin.toString(),
            )
        }

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
            Log.d("time", item.time)
            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                text = item.time.dropLast(3),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Image(
                painter = painterResource(Utils.getImage(item.img)),
                null,
                modifier = Modifier.size(70.dp)
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
            painter = painterResource(id = Utils.getImage(img)),
            contentDescription = "null",
            modifier = Modifier.size(60.dp)
        )
        Text(text = "${temp_high}°C", color = Color.White)
        Text(text = "${temp_low}°C", color = Color.White)
    }
}

@Composable
fun DetailsCard(forcast: ApiResponse?) {
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
    ) {
        Text(
            "Details",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Utils.getImage(forcast?.currentConditions?.icon.toString())),
                    null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Feels like: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast?.currentConditions?.feelslike.toString(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Humidity: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast?.currentConditions?.humidity.toString(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Visibility: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast?.currentConditions?.visibility.toString(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "UV index: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast?.currentConditions?.uvindex.toString(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Dew Point: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast?.currentConditions?.dew.toString(),
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        Text(
            forcast?.description.toString(),
            modifier = Modifier.padding(4.dp),
            color = Color.Gray
        )
    }

}

@Composable
fun Custom_divider() {
    Divider(
        color = text_right_grad,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}


//@Preview(showBackground = true)
//@Composable
//fun preview(modifier: Modifier = Modifier) {
//    dailyForecastView()
//}
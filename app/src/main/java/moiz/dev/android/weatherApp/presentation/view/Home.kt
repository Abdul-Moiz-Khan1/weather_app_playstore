package moiz.dev.android.weatherApp.presentation.view

import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import moiz.dev.android.weatherApp.data.viewModel.WeatherViewModel
import androidx.compose.ui.draw.clip
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
import androidx.hilt.navigation.compose.hiltViewModel
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
import moiz.dev.android.weatherApp.utils.Routes
import moiz.dev.android.weatherApp.utils.Utils
import moiz.dev.android.weatherApp.utils.Utils.getDailyForecastItems
import moiz.dev.android.weatherApp.utils.Utils.getLocationName
import moiz.dev.android.weatherApp.utils.Utils.tempToInt

@Composable
fun Home(
    navController: NavController, viewModel: WeatherViewModel
) {

    val forcast = viewModel.forecast.observeAsState()
    Log.d("CatchError_in_home", forcast.value.toString())
    Log.d("CatchError_size", forcast.value?.days?.size.toString())
    Log.d("CatchErrorHome_permissions", "location ${viewModel.locationPermission}, internet ${viewModel.internet}")
    if (!viewModel.internet) {
        Toast.makeText(LocalContext.current, "No Internet Connection", Toast.LENGTH_SHORT).show()
        Log.d("CatchError_in_home", "no internet")

        viewModel.loadCacheData()

        if (forcast.value == null) {
            ShowNoData(
                R.drawable.no_internet,
                "No Internet Connection",
                false,
                viewModel
            )
        } else {
            ShowUi(navController, forcast.value, viewModel)
        }

    } else {
        Log.d("CatchError_in_home", "yes internet")
        viewModel.loadCacheData()
        if (!viewModel.locationPermission) {
            if (forcast.value == null) {
                Log.d("CatchError_in_home", "no location search city")
                ShowNoData(
                    R.drawable.no_location,
                    "Location Not found\n\nTry searching",
                    true,
                    viewModel
                )
            } else {
                ShowUi(navController, forcast.value, viewModel)
            }
        } else {
            Log.d("CatchError_in_home", "yes internet show ui")
            ShowUi(navController, forcast.value, viewModel)
        }
    }

//    if (!viewModel.internet) {
//        Toast.makeText(LocalContext.current, "No Internet Connection", Toast.LENGTH_SHORT).show()
//        Log.d("CatchError_in_home", "no internet")
//        viewModel.loadCacheData()
//        if (forcast.value == null) {
//            ShowNoData(R.drawable.no_internet, "No Internet Connection" , false , viewModel)
//        } else {
//            ShowUi(navController, forcast.value, viewModel)
//        }
//    } else {
//        Log.d("CatchError_in_home", "yes internet")
//        if (!viewModel.locationPermission) {
//            if (forcast.value == null) {
//                Log.d("CatchError_in_home", "no location search city" )
//                ShowNoData(R.drawable.no_location, "Location Not found\n\nTry searching", true , viewModel)
//            }
//        }else {
//            Log.d("CatchError_in_home", "yes internet show ui")
//            ShowUi(navController, forcast.value, viewModel)
//        }
//    }


}

@Composable
fun ShowNoData(image: Int, text: String ,searchable:Boolean , viewModel: WeatherViewModel) {
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
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    ) {
        if(searchable){
            CustomSearch(viewModel)
        }
        Image(
            painter = painterResource(image),
            contentDescription = "",
            modifier = Modifier.size(300.dp)
        )
        Text(
            text,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
            fontSize = 32.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun CustomSearch(viewModel: WeatherViewModel) {
    var searchText by remember { mutableStateOf("") }
    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Search location...") },
        singleLine = true,
        shape = RoundedCornerShape(30.dp),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.Gray,
            focusedContainerColor = cards_bg,
            unfocusedContainerColor = cards_bg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.DarkGray,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(4.dp),
        trailingIcon = {
            IconButton(onClick = {
            }) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    tint = Color.Gray, modifier = Modifier.clickable {
                        viewModel.loadForcast(searchText , 6)
                    }
                )
            }
        }
    )
}

@Composable
fun ShowUi(navController: NavController, forcast: ApiResponse?, viewModel: WeatherViewModel) {
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
        val dailyForecastList = getDailyForecastItems(forcast?.days?.get(0))
        Spacer(modifier = Modifier.height(8.dp))
        LocationSearchBar(forcast, viewModel)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Utils.getImage(forcast?.currentConditions?.conditions.toString())),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        "Feels like ${forcast?.currentConditions?.feelslike}°C",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "${Utils.tempToInt(forcast?.currentConditions?.temp)}°",
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
                    Text(
                        "Wind ${forcast?.currentConditions?.windspeed} Km/h",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Light
                    )
                }
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
                Attribute(
                    R.drawable.precipitation,
                    "Precipitation",
                    "${forcast?.currentConditions?.precip} mm"
                )
                Attribute(R.drawable.wind, "Wind", "${forcast?.currentConditions?.windspeed} Km/h")


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Attribute(
                    R.drawable.humidity,
                    "Humidity",
                    "${forcast?.currentConditions?.humidity}%"
                )

                Attribute(
                    R.drawable.sunset,
                    "Sunset",
                    forcast?.currentConditions?.sunset.toString().dropLast(3)
                )

            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Custom_divider()
        ScrollableRow(dailyForecastList)
        Spacer(modifier = Modifier.height(8.dp))
        WeeklyForecastCard(forcast)
        Spacer(modifier = Modifier.height(8.dp))
        GotoForecastCard(navController)
        Spacer(modifier = Modifier.height(8.dp))
        DetailsCard(forcast)

    }

}

@Composable
fun LocationSearchBar(forcast: ApiResponse?, viewModel: WeatherViewModel) {
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    if (showSearch) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search location...") },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.Gray,
                focusedContainerColor = cards_bg,
                unfocusedContainerColor = cards_bg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.DarkGray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(4.dp),
            trailingIcon = {
                IconButton(onClick = {
                    showSearch = false
                }) {
                    val context = LocalContext.current
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = null,
                        tint = Color.Gray, modifier = Modifier.clickable {
                            if(!viewModel.internet){
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
                                return@clickable
                            }else{
                                viewModel.loadForcast(searchText, 7)
                            }
                        }
                    )
                }
            }
        )

    } else {
        val address = forcast?.address
        var locationName by remember { mutableStateOf<String?>(address) }
        val context = LocalContext.current
        LaunchedEffect(address) {
            if (!forcast?.address.isNullOrEmpty() && address.contains(",")) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .clip(RoundedCornerShape(30.dp))
                .background(cards_bg)
                .padding(12.dp)
                .clickable { /* Optional: maybe open search too */ },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = locationName ?: "Unknown location",
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(R.drawable.search),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        showSearch = true
                    }
            )
        }
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
                        main_card_grad_bottom,
                        main_card_grad_bottom,
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
            DailyForecastView(
                item, modifier = Modifier
                    .height(160.dp)
                    .width(100.dp), 70
            )
        }
    }
}

@Composable
fun Attribute(image: Int, attribute: String, value: String) {
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
fun DailyForecastView(item: DailyForecastItem, modifier: Modifier, iconSize: Int) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
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
                modifier = Modifier.size(iconSize.dp)
            )


            Text(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                text = tempToInt(item.temp.toDouble()).toString() + "°",
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
        Text(text = "${tempToInt(temp_high.toDouble())}°C", color = Color.White)
        Text(text = "${tempToInt(temp_low.toDouble())}°C", color = Color.White)
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
                        main_card_grad_bottom,
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
                    contentScale = ContentScale.Fit
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
                        forcast?.currentConditions?.feelslike.toString() + "°",
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
                        tempToInt(forcast?.currentConditions?.humidity).toString() + "%",
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
                        forcast?.currentConditions?.visibility?.let {
                            tempToInt(
                                it.toString().toDouble()
                            )
                            "m"
                        } ?: "Good",
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
                        "${tempToInt(forcast?.currentConditions?.dew)}°",
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
fun GotoForecastCard(navController: NavController) {
    Row(
        modifier = Modifier
            .clickable {
                navController.navigate(Routes.DETAILS)
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(40.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        main_card_grad_bottom,
                        main_card_grad_bottom,
                        main_card_grad_top
                    )
                )
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 16.dp, top = 4.dp, bottom = 4.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                "See day by day forecasts",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Plan for next 7 days",
                fontSize = 12.sp,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
            )
        }
        Column(
            modifier = Modifier
                .weight(0.2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(main_card_grad_bottom),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.next),
                    contentDescription = "Arrow",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(10.dp)
                )
            }

        }
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

//
//@Preview(showBackground = true)
//@Composable
//fun preview(modifier: Modifier = Modifier) {
//    ShowNoInternet()
//}
package com.ttl.weatherupdate.forecast.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ttl.weatherupdate.forecast.data.viewModel.WeatherViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.ttl.weatherupdate.forecast.data.model.DailyForecastItem
import com.ttl.weatherupdate.forecast.ui.theme.cards_bg
import com.ttl.weatherupdate.forecast.ui.theme.grad_home_above
import com.ttl.weatherupdate.forecast.ui.theme.grad_home_below
import com.ttl.weatherupdate.forecast.ui.theme.main_card_grad_bottom
import com.ttl.weatherupdate.forecast.ui.theme.main_card_grad_top
import com.ttl.weatherupdate.forecast.ui.theme.text_left_grad
import com.ttl.weatherupdate.forecast.ui.theme.text_right_grad
import com.ttl.weatherupdate.forecast.utils.Utils
import com.ttl.weatherupdate.forecast.utils.Utils.ShowLoading 
import com.ttl.weatherupdate.forecast.utils.Utils.getDailyForecastItems
 
import com.ttl.weatherupdate.forecast.utils.Utils.setSeenOnboarding
import com.ttl.weatherupdate.forecast.utils.Utils.tempToInt

import com.ttl.weatherupdate.forecast.R
import com.ttl.weatherupdate.forecast.data.model.CitySuggestion
import com.ttl.weatherupdate.forecast.data.model.DailyForecast
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast 
import com.ttl.weatherupdate.forecast.utils.Utils.getDayNameFromDate 
import com.ttl.weatherupdate.forecast.utils.Utils.getSavedCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import java.time.LocalTime

@Composable
fun Home(
    navController: NavController, viewModel: WeatherViewModel
) {
    val context = LocalContext.current


    setSeenOnboarding(context)
    val internet = viewModel.internet
    val locationperm = viewModel.locationPermission
    val loading = viewModel.isLoading

    val weeklyForecasts by viewModel.weeklyForecasts.observeAsState(emptyList())
    val hourlyForecasts by viewModel.hourlyForecasts.observeAsState(emptyList())

    Log.d(
        "CatchErrorHome",
        "size ${weeklyForecasts.size} weeklyforecast from web: ${weeklyForecasts.toString()}"
    )
    Log.d(
        "CatchErrorHome",
        "size ${hourlyForecasts.size} hourly forecast from web: ${hourlyForecasts.toString()}"
    )

    Log.d("CatchErrorHome_permissions", "internet: $internet")
    Log.d("CatchErrorHome_permissions", "location: $locationperm")

    LaunchedEffect(Unit) {
        viewModel.loadForecastsFromCache()
    }

    if (weeklyForecasts.isEmpty() || hourlyForecasts.isEmpty()) {
        ShowLoading() // Optional while waiting
    } else if (loading) {
        Log.d("CatchError_in_home", "loading_NoInternet")
        ShowLoading()
    } else if (!internet) {
        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show()
        Log.d("CatchError_in_home", "no internet")
        if (weeklyForecasts.isEmpty() || hourlyForecasts.isEmpty()) {
            try {
                viewModel.loadForecastsFromCache()
            } catch (e: Exception) {
                Log.d("CatchError_in_home", "catch_NoInternet: ${e.message}")
            }
            ShowNoData(
                R.drawable.no_internet,
                "No Internet Connection",
                false,
                viewModel
            )
        } else {
            ShowUi(navController, weeklyForecasts, viewModel, hourlyForecasts)
        }
    } else {
        Log.d("CatchError_in_home", "yes internet")
        if (!locationperm) {
            if (weeklyForecasts.isEmpty() || hourlyForecasts.isEmpty()) {
                try {
                    viewModel.loadForecastsFromCache()
                } catch (e: Exception) {
                    Log.d("CatchError_in_home", "catch_NoInternet: ${e.message}")
                }
                Log.d("CatchError_in_home", "no location search city")
                ShowNoData(
                    R.drawable.no_location,
                    "Location Not found\n\nTry searching",
                    true,
                    viewModel
                )
            } else {
                ShowUi(navController, weeklyForecasts, viewModel, hourlyForecasts)
            }
        } else {
            Log.d("CatchError_in_home", "yes internet show ui")
            if (weeklyForecasts.isEmpty() || hourlyForecasts.isEmpty()) {
                ShowLoading()
            } else {
                ShowUi(navController, weeklyForecasts, viewModel, hourlyForecasts)
            }
        }
    }


}


@Composable
fun ShowNoData(image: Int, text: String, searchable: Boolean, viewModel: WeatherViewModel) {
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
        if (searchable) {
            LocationSearchBar(viewModel)
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
fun ShowUi(
    navController: NavController,
    forcast: List<DailyForecast>,
    viewModel: WeatherViewModel,
    hourlyForecasts: List<HourlyForecast>
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
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
            .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val dailyForecastList = getDailyForecastItems(hourlyForecasts)
        Spacer(modifier = Modifier.height(8.dp))
        LocationSearchBar(viewModel)

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
                        painter = painterResource(Utils.getImage(forcast[0].condition, "3pm")),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        "Feels like ${forcast.get(0).feelsLike}",
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
                        "${Utils.tempToInt(forcast.get(0).maxTemp.toDouble())}°",
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
                        forcast.get(0).condition,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        "Wind ${forcast[0].windSpeed}",
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
                    "Rain",
                    "${forcast[0].rainChance}"
                )
                Attribute(R.drawable.wind, "Wind", "${forcast[0].windSpeed}")


            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Attribute(
                    R.drawable.humidity,
                    "Humidity",
                    "${forcast[0].humidity}"
                )

                Attribute(
                    R.drawable.sunset,
                    "Sunset",
                    forcast[0].sunset
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
fun LocationSearchBar(viewModel: WeatherViewModel) {
    var showSearch by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<CitySuggestion>>(emptyList()) }
    var isFocused by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(query) {
        snapshotFlow { query }
            .debounce(500)
            .collectLatest { city ->
                if (city.length >= 2) {
                    suggestions = viewModel.fetchSuggestions(city)
                } else {
                    suggestions = emptyList()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showSearch) {
                // Search suggestions text field
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        isFocused = true
                    },
                    label = { Text("Enter city") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.7f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        }
                )


            } else {
                // Your rounded search TextField
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("${getSavedCity(context)}" , modifier = Modifier.fillMaxWidth() , textAlign = TextAlign.Center) },
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
                            showSearch = true // Switch to suggestions mode
                            viewModel.location_city = searchText.trim()
                            searchText = ""
                        }) {
                            Icon(
                                painter = painterResource(R.drawable.search),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    }
                )
            }
        }

        if (showSearch && suggestions.isNotEmpty() && isFocused) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .padding(horizontal = 16.dp)
                    .absoluteOffset(y = 64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(grad_home_above, grad_home_below)
                        )
                    )
            ) {
                items(suggestions) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                query = item.name
                                isFocused = false
                                suggestions = emptyList()
                                focusManager.clearFocus()
                                viewModel.SearchDatafromWeb(item.id)
                                viewModel.SearchHourlyData(context, item.id)
                                showSearch = false
                            }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ID: ${item.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyForecastCard(forcast: List<DailyForecast>) {
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
                forcast[i].date,
                forcast[i].condition,
                forcast[i].maxTemp,
                forcast[i].minTemp,
            )
        }

    }
}

@Composable
fun ScrollableRow(list1: List<DailyForecastItem>) {
    val list = list1

    var selectedItem by remember { mutableStateOf<DailyForecastItem?>(null) }
    val listState = rememberLazyListState()
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(list) { item: DailyForecastItem ->
            DailyForecastView(
                item, modifier = Modifier
                    .height(160.dp)
                    .width(100.dp)
                    .clickable { selectedItem = item }, 70
            )
        }

    }
    selectedItem?.let { item ->
        WeatherDetailDialog(item) {
            selectedItem = null
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
                text = item.time,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )

            Image(
                painter = painterResource(Utils.getImage(item.img, item.time)),
                null,
                modifier = Modifier.size(iconSize.dp)
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
fun WeeklyForecastView(
    date: String,
    img: String,
    temp_high: String,
    temp_low: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = getDayNameFromDate(date), color = Color.White)
        Image(
            painter = painterResource(id = Utils.getImage(img, "2pm")),
            contentDescription = "null",
            modifier = Modifier.size(60.dp)
        )
        Text(text = "${temp_high} °C", color = Color.White)
        Text(text = temp_low, color = Color.White)
    }
}

@Composable
fun DetailsCard(forcast: List<DailyForecast>) {
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
                modifier = Modifier.weight(0.35f),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Utils.getImage(forcast[0].condition, "2pm")),
                    null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Column(modifier = Modifier.weight(0.65f)) {
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
                        forcast[0].feelsLike,
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
                        forcast[0].humidity,
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
                        "Rain Chances: ",
                        color = Color.Gray,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        forcast[0].rainChance,
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
                        forcast[0].uvIndex,
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
                        "${forcast[0].humidity}",
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
        Text(
            forcast[0].condition,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            textAlign = TextAlign.Center,
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

@Composable
fun WeatherDetailDialog(item: DailyForecastItem, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            grad_home_above,
                            grad_home_below
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Weather at ${item.time}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Image(
                    painter = painterResource(id = Utils.getImage(item.condition, item.time)),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    " ${item.condition}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp, textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("🌡 Temp: ${item.temperature}", color = Color.White)
                Text("🤒 Feels like: ${item.feelsLike}", color = Color.White)
                Text("🌧 Rain chance: ${item.rainChance}", color = Color.White)
                Text("💨 Wind: ${item.wind}", color = Color.White)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Close", color = Color.Black)
                }
            }
        }
    }
}



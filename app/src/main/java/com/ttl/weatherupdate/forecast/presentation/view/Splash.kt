package com.ttl.weatherupdate.forecast.presentation.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import com.ttl.weatherupdate.forecast.utils.Utils.getCurrentLocation
import com.ttl.weatherupdate.forecast.data.viewModel.WeatherViewModel
import com.ttl.weatherupdate.forecast.ui.theme.left_grad
import com.ttl.weatherupdate.forecast.utils.Routes
import com.ttl.weatherupdate.forecast.utils.Utils.ShowLoading
import com.ttl.weatherupdate.forecast.utils.Utils.hasSeenOnboarding
import com.ttl.weatherupdate.forecast.utils.Utils.isLocationEnabled
import com.ttl.weatherupdate.forecast.utils.Utils.setSeenOnboarding
import okhttp3.Route

import com.ttl.weatherupdate.forecast.R
import com.ttl.weatherupdate.forecast.utils.Utils
import com.ttl.weatherupdate.forecast.utils.Utils.getLocationName

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Splash(navController: NavController, viewModel: WeatherViewModel) {

    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(!hasSeenOnboarding(context)) }
    LaunchedEffect(Unit) {
        delay(1500)
        if (showOnboarding) {
            navController.navigate(Routes.ONBOARDING) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.SPLASH) { inclusive = true }
            }
        }
    }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation(context, onLocation = { lat, lon ->
                Log.d("Location", "Latitude: $lat, Longitude: $lon")
                viewModel.latitude = lat
                viewModel.longitude = lon
                val city = getLocationName(context, lat.toDouble(), lon.toDouble(), viewModel)
                var country = Utils.getCountryFromCity(context, city.toString(), viewModel)
                Log.d("country:${country} , city: ${city}" , "$country $city")
                viewModel.getDatafromWeb(context,city.toString() , country.toString())
                viewModel.getHourlyData(context,city.toString() , country.toString())
            }, onError = {
                Log.d("Location", "Error: $it")
                viewModel.locationPermission = false
            })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        left_grad,
                        androidx.compose.ui.graphics.Color.White,

                        )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(painter = painterResource(R.drawable.splash_img), contentDescription = "")
        Text(
            "Weather",
            fontSize = 40.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            "Forecast",
            fontSize = 24.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal
        )


    }
}




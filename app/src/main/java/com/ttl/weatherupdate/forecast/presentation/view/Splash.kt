package com.ttl.weatherupdate.forecast.presentation.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay 
import com.ttl.weatherupdate.forecast.data.viewModel.WeatherViewModel
import com.ttl.weatherupdate.forecast.ui.theme.left_grad
import com.ttl.weatherupdate.forecast.utils.Routes
import com.ttl.weatherupdate.forecast.utils.Utils.hasSeenOnboarding
import com.ttl.weatherupdate.forecast.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Splash(navController: NavController, viewModel: WeatherViewModel) {

    val context = LocalContext.current
    var showOnboarding by remember { mutableStateOf(!hasSeenOnboarding(context)) }
    LaunchedEffect(Unit) {
        viewModel.getDatafromWeb(context)
        viewModel.getHourlyData()
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




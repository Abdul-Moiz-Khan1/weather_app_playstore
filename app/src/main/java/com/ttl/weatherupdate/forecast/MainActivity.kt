package com.ttl.weatherupdate.forecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import com.ttl.weatherupdate.forecast.data.viewModel.WeatherViewModel
import com.ttl.weatherupdate.forecast.presentation.view.Home
import com.ttl.weatherupdate.forecast.presentation.view.OnBoarding
import com.ttl.weatherupdate.forecast.presentation.view.Splash
import com.ttl.weatherupdate.forecast.ui.theme.WeatherApp_playStoreTheme
import com.ttl.weatherupdate.forecast.utils.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WeatherApp_playStoreTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.SPLASH
                ) {
                    composable(Routes.SPLASH) { Splash(navController , viewModel)  }
                    composable(Routes.HOME) { Home(navController , viewModel) }
                    composable(Routes.ONBOARDING) { OnBoarding(navController) }
                }
            }
        }
    }

}




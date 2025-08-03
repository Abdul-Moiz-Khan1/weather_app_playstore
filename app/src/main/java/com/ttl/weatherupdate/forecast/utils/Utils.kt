package com.ttl.weatherupdate.forecast.utils

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.location.LocationServices
import com.ttl.weatherupdate.forecast.R
import com.ttl.weatherupdate.forecast.data.model.DailyForecastItem
import com.ttl.weatherupdate.forecast.ui.theme.grad_home_above
import com.ttl.weatherupdate.forecast.ui.theme.grad_home_below
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.core.content.edit
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast
import com.ttl.weatherupdate.forecast.data.viewModel.WeatherViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

object Utils {
    fun getCurrentLocation(
        context: Context,
        onLocation: (String, String) -> Unit,
        onError: (Boolean) -> Unit
    ) {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onError(true)
                    onLocation(location.latitude.toString(), location.longitude.toString())
                } else {
                    onError(false)
                    Log.e("LocationError_Utils", "Location is null")
                }
            }
        } catch (e: SecurityException) {
            Log.e("LocationError_Utils", "Permission denied: ${e.message}")
        }
    }

    fun getDayNameFromDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("d MMM", Locale.ENGLISH)
            val parsedDate = inputFormat.parse(date)

            // Add the current year to make it a complete date
            val calendar = Calendar.getInstance()
            calendar.time = parsedDate!!
            calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))

            val outputFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
            outputFormat.format(calendar.time)
        } catch (e: Exception) {
            "Unknown"
        }
    }

    fun getImage(condition: String): Int {
        return when {
            condition.contains("sunny", ignoreCase = true) -> R.drawable.sunny
            condition.contains("clear", ignoreCase = true) -> R.drawable.day_clear
            condition.contains("cloudy", ignoreCase = true) -> R.drawable.day_cloudy
            condition.contains("rain", ignoreCase = true) -> R.drawable.rain
            condition.contains("patchy rain", ignoreCase = true) -> R.drawable.patchy_rain
            condition.contains("wind", ignoreCase = true) -> R.drawable.windy_cloudy
            condition.contains("thunder", ignoreCase = true) -> R.drawable.thunderstorm
            condition.contains("lightning", ignoreCase = true) -> R.drawable.lightning
            condition.contains("fog", ignoreCase = true) -> R.drawable.fog
            condition.contains("snow", ignoreCase = true) -> R.drawable.snow
            condition.contains("blizzard", ignoreCase = true) -> R.drawable.snow
            else -> R.drawable.splash_img
        }
    }

    fun tempToInt(temp: Double?): Int? {
        return temp?.toInt()
    }

    fun getDailyForecastItems(day: List<HourlyForecast>): List<DailyForecastItem> {
        return day.mapIndexed { index, hourly ->
            DailyForecastItem(
                time = formatHour(index),
                img = hourly.condition,
                temp = hourly.temperature,
                temperature = hourly.temperature,
                condition = hourly.condition,
                feelsLike = hourly.feelsLike,
                wind = hourly.wind,
                rainAmount = hourly.rainAmount,
                rainChance = hourly.rainChance
            )
        }
    }

    @Composable
    fun ShowLoading() {
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

    fun hasSeenOnboarding(context: Context): Boolean {
        val prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        return prefs.getBoolean("seen", false)
    }

    fun setSeenOnboarding(context: Context) {
        val prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        prefs.edit { putBoolean("seen", true) }
    }

    fun getLocationName(context: Context, lat: Double, lon: Double , viewModel: WeatherViewModel): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)

            Log.d("Utils_CatchError_getCountry", addresses?.get(0)?.locality.toString())
            viewModel.location_city = addresses?.get(0)?.locality.toString()
            addresses?.get(0)?.locality

        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getCountryFromCity(context: Context, cityName: String, viewModel: WeatherViewModel): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocationName(cityName, 1)
            if (!addresses.isNullOrEmpty()) {
                Log.d("Utils_CatchError_getCountry", addresses[0].countryName.toString())
                viewModel.location_country = addresses[0].countryName
                addresses[0].countryName

            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun formatHour(index: Int): String {
        val hour24 = index % 24  // just in case list has more than 24 entries
        val isAM = hour24 < 12
        val hour12 = when (hour24 % 12) {
            0 -> 12
            else -> hour24 % 12
        }
        val period = if (isAM) "am" else "pm"
        return "$hour12$period"
    }


}
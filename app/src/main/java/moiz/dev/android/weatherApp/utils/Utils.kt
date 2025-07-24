package moiz.dev.android.weatherApp.utils

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
import moiz.dev.android.weatherApp.R
import moiz.dev.android.weatherApp.data.model.DailyForecastItem
import moiz.dev.android.weatherApp.data.model.weatherResponse.Day
import moiz.dev.android.weatherApp.ui.theme.grad_home_above
import moiz.dev.android.weatherApp.ui.theme.grad_home_below
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.core.content.edit
import java.io.IOException

object Utils {
    fun getCurrentLocation(context: Context, onLocation: (String, String) -> Unit , onError:(Boolean)->Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
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

    fun getDayOfWeek(dateString: String?): String {
        return try {
            if (dateString.isNullOrBlank() || dateString == "null") {
                ""
            } else {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                val date = LocalDate.parse(dateString, formatter)
                date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
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

    fun getDailyForecastItems(day: Day?): List<DailyForecastItem> {
        val hours = day?.hours ?: return emptyList()

        return hours.take(24).map { hour ->
            DailyForecastItem(
                time = hour.datetime ?: "--:--",
                img = hour.icon ?: "unknown",
                temp = hour.temp?.toString() ?: "--"
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

    fun getLocationName(context: Context, lat: Double, lon: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            addresses?.get(0)?.locality
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

}
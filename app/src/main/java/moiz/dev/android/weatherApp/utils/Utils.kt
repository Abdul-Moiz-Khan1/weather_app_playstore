package moiz.dev.android.weatherApp.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import moiz.dev.android.weatherApp.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object Utils {
    fun getCurrentLocation(context: Context, onLocation: (String, String) -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    onLocation(location.latitude.toString(), location.longitude.toString())
                } else {
                    Log.e("LocationError_Utils", "Location is null")
                }
            }
        } catch (e: SecurityException) {
            Log.e("LocationError_Utils", "Permission denied: ${e.message}")
        }
    }

    fun getDayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    fun getImage(condition: String): Int {
        return when {
            condition.contains("sunny", ignoreCase = true) -> R.drawable.sunny
            condition.contains("clear", ignoreCase = true) -> R.drawable.sunny
            condition.contains("cloudy", ignoreCase = true) -> R.drawable.partly_cloudy
            condition.contains("rain", ignoreCase = true) -> R.drawable.rainy
            condition.contains("wind", ignoreCase = true) -> R.drawable.windy_cloudy
            condition.contains("thunder", ignoreCase = true) -> R.drawable.thunderstorm
            condition.contains("fog", ignoreCase = true) -> R.drawable.fog
            else -> R.drawable.splash_img
        }
    }

}
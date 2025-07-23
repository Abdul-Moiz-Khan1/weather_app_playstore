package moiz.dev.android.weatherApp

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.LocationServices
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

}
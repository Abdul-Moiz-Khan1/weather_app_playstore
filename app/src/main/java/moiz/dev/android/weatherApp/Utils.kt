package moiz.dev.android.weatherApp

import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices

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

}
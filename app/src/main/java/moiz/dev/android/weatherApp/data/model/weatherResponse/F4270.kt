package moiz.dev.android.weatherApp.data.model.weatherResponse

import androidx.annotation.Keep

@Keep
data class F4270(
    val contribution: Int,
    val distance: Int,
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val quality: Int,
    val useCount: Int
)
package moiz.dev.android.weatherApp.data.model.weatherResponse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApiResponse(
    @PrimaryKey val id: Int = 0,
    val address: String,
    val alerts: List<Any>,
    val currentConditions: CurrentConditions,
    val days: List<Day>,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val queryCost: Int,
    val resolvedAddress: String,
    val stations: Stations,
    val timezone: String,
    val tzoffset: Int
)
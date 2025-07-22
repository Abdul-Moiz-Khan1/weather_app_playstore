package moiz.dev.android.weatherApp.data.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Weather(

    @PrimaryKey
    val id: Int = 1,
    val current: Current,
    val forecast: Forecast,
    val location: Location
)
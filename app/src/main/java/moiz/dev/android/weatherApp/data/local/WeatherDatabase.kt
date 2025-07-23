package moiz.dev.android.weatherApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse

@Database(entities = [ApiResponse::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
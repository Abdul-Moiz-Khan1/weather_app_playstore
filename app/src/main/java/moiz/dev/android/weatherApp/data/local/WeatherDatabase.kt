package moiz.dev.android.weatherApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import moiz.dev.android.weatherApp.data.model.response.Weather

@Database(entities = [Weather::class], version = 1, exportSchema = false)
@TypeConverters(TypeConvertors::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
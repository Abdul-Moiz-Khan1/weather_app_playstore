package moiz.dev.android.weatherApp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse

@Dao
@TypeConverters(WeatherTypeConverters::class)
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: ApiResponse)

    @Query("SELECT * FROM ApiResponse Where id=0")
    suspend fun getCacheWeather(): ApiResponse?
    //QUESTION: Why was i not getting a response when i put id=1

    @Query("SELECT * FROM ApiResponse")
    suspend fun getAll(): List<ApiResponse>
}
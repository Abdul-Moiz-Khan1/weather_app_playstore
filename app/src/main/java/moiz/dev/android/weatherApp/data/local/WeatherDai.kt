package moiz.dev.android.weatherApp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import moiz.dev.android.weatherApp.data.model.response.Weather

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: Weather)

    @Query("SELECT * FROM Weather Where id=0")
    suspend fun getCacheWeather(): Weather?
    //QUESTION: Why was i not getting a response when i put id=1


    @Query("SELECT * FROM Weather")
    suspend fun getAll(): List<Weather>
}
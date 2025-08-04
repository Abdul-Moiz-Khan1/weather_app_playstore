package com.ttl.weatherupdate.forecast.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.ttl.weatherupdate.forecast.data.model.DailyForecast
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast

@Dao
interface WeatherDao {

    // Hourly
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyForecast(hourly: List<HourlyForecast>)

    @Query("SELECT * FROM hourly_forecast")
    suspend fun getHourlyForecast(): List<HourlyForecast>

    @Query("DELETE FROM hourly_forecast")
    suspend fun clearHourly()

    // Daily
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyForecast(daily: List<DailyForecast>)

    @Query("SELECT * FROM daily_forecast")
    suspend fun getDailyForecast(): List<DailyForecast>

    @Query("DELETE FROM daily_forecast")
    suspend fun clearDaily()
}
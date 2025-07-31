package com.ttl.weatherupdate.forecast.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ttl.weatherupdate.forecast.data.model.weatherResponse.ApiResponse

@Database(entities = [ApiResponse::class], version = 1, exportSchema = false)
@TypeConverters(WeatherTypeConverters::class)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
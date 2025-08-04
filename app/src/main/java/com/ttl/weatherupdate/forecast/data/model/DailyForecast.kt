package com.ttl.weatherupdate.forecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "daily_forecast")
data class DailyForecast(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val day: String,
    val date: String,
    val minTemp: String,
    val maxTemp: String,
    val condition: String,
    val feelsLike: String,
    val windSpeed: String,
    val humidity: String,
    val rainChance: String,
    val rainAmount: String,
    val uvIndex: String,
    val sunrise: String,
    val sunset: String
)


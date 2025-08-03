package com.ttl.weatherupdate.forecast.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "hourly_forecast")
data class HourlyForecast(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val time: String,
    val temperature: String,
    val condition: String,
    val feelsLike: String,
    val wind: String,
    val humidity: String,
    val rainChance: String,
    val rainAmount: String
)


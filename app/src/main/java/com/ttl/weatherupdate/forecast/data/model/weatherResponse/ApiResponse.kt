package com.ttl.weatherupdate.forecast.data.model.weatherResponse

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ttl.weatherupdate.forecast.data.local.WeatherTypeConverters

@Keep
@Entity
@TypeConverters(WeatherTypeConverters::class)
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
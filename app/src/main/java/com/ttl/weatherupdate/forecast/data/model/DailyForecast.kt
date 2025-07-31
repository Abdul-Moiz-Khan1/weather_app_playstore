package com.ttl.weatherupdate.forecast.data.model

data class DailyForecast(
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


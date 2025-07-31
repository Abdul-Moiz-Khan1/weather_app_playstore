package com.ttl.weatherupdate.forecast.data.model

data class HourlyForecast(
    val time: String,
    val temperature: String,
    val condition: String,
    val feelsLike: String,
    val wind: String,
    val humidity: String,
    val rainChance: String,
    val rainAmount: String
)


package com.ttl.weatherupdate.forecast.data.model

data class DailyForecastItem(
    val time: String,
    val img: String,
    val temp: String,
    val temperature: String,
    val condition: String,
    val feelsLike: String,
    val wind: String,
    val rainChance: String,
    val rainAmount: String
)

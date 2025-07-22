package moiz.dev.android.weatherApp.data.model.response

import moiz.dev.android.weatherApp.data.model.response.Astro

data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)
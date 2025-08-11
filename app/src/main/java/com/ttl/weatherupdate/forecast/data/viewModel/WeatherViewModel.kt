package com.ttl.weatherupdate.forecast.data.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ttl.weatherupdate.forecast.data.local.WeatherDao
import com.ttl.weatherupdate.forecast.data.model.CitySuggestion
import com.ttl.weatherupdate.forecast.data.model.DailyForecast
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.ttl.weatherupdate.forecast.data.repository.WeatherRepository
import com.ttl.weatherupdate.forecast.utils.NetworkStatusTracker
import com.ttl.weatherupdate.forecast.utils.Utils.setSavedCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.toString

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val networkStatusTracker: NetworkStatusTracker,
    private val dao: WeatherDao
) :
    ViewModel() {

    private val _internet = mutableStateOf(true)
    val internet: Boolean get() = _internet.value

    init {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collect { isConnected ->
                _internet.value = isConnected
            }
        }
    }

    private val _weeklyForecasts = MutableLiveData<List<DailyForecast>>()
    val weeklyForecasts: LiveData<List<DailyForecast>> get() = _weeklyForecasts

    private val _hourlyForecasts = MutableLiveData<List<HourlyForecast>>()
    val hourlyForecasts: LiveData<List<HourlyForecast>> get() = _hourlyForecasts

    var isLoading by mutableStateOf(false)

    var location_city by mutableStateOf("")
    var location_country by mutableStateOf("")

    var locationPermission by mutableStateOf(false)

    fun getDatafromWeb(context: Context) {

        viewModelScope.launch {
            isLoading = true
            try {
                repository.getWeeklyDatafromWeb(context,
                    onSucess = { elements ->
                        val allForecasts = mutableListOf<DailyForecast>()

                        for (row in elements) {
                            val dayAndDate = row.select("th").text().split(" ", limit = 2)
                            val data = row.select("td").map { it.text() }.drop(1)

                            if (dayAndDate.size == 2 && data.size >= 11) {
                                val temps = data[0].split("/")
                                val forecast = DailyForecast(
                                    day = dayAndDate[0],
                                    date = dayAndDate[1],
                                    maxTemp = temps.getOrNull(0)?.trim() ?: "",
                                    minTemp = temps.getOrNull(1)?.trim() ?: "",
                                    condition = data[1],
                                    feelsLike = data[2],
                                    windSpeed = data[3] + " " + data[4],
                                    humidity = data[5],
                                    rainChance = data[6],
                                    rainAmount = data[7],
                                    uvIndex = data[8],
                                    sunrise = data[9],
                                    sunset = data[10]
                                )
                                allForecasts.add(forecast)
                            }
                        }

                        _weeklyForecasts.postValue(allForecasts) // 🔥 Post once only
                        viewModelScope.launch {
                            saveForecastsDaily(weeklyForecasts.value)
                        }
                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_from_web: $it")
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError get_from_web: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun getHourlyData() {

        viewModelScope.launch {
            isLoading = true
            try {
                repository.getHourlyForecastData(

                    onSucess = { rows_hourly ->

                        val allForecasts = mutableListOf<HourlyForecast>()
                        for (row in rows_hourly) {
                            val cells = row.select("td")
                            if (cells.size >= 8) {
                                val forecast = HourlyForecast(
                                    time = cells[0].text(),
                                    temperature = cells[1].text(),
                                    condition = cells[2].text(),
                                    feelsLike = cells[3].text(),
                                    wind = cells[4].text(),
                                    humidity = cells[5].text(),
                                    rainChance = cells[6].text(),
                                    rainAmount = cells[7].text()
                                )
                                allForecasts.add(forecast)
                            }
                        }
                        _hourlyForecasts.postValue(allForecasts) // ✅ Post once after loop
                        viewModelScope.launch {
                            saveForecastsHourly(hourlyForecasts.value)
                        }

                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_hourly_data: $it")
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError GET_HOURLY: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun SearchDatafromWeb(url: String) {
        Log.d("in_viewmodel" , "getDatafromweb ${location_country} ${location_city}")

        viewModelScope.launch {
            isLoading = true
            try {
                repository.SearchWeeklyDatafromWeb(
                    url,
                    onSucess = { elements ->
                        val allForecasts = mutableListOf<DailyForecast>()

                        for (row in elements) {
                            val dayAndDate = row.select("th").text().split(" ", limit = 2)
                            val data = row.select("td").map { it.text() }.drop(1)

                            if (dayAndDate.size == 2 && data.size >= 11) {
                                val temps = data[0].split("/")
                                val forecast = DailyForecast(
                                    day = dayAndDate[0],
                                    date = dayAndDate[1],
                                    maxTemp = temps.getOrNull(0)?.trim() ?: "",
                                    minTemp = temps.getOrNull(1)?.trim() ?: "",
                                    condition = data[1],
                                    feelsLike = data[2],
                                    windSpeed = data[3] + " " + data[4],
                                    humidity = data[5],
                                    rainChance = data[6],
                                    rainAmount = data[7],
                                    uvIndex = data[8],
                                    sunrise = data[9],
                                    sunset = data[10]
                                )
                                allForecasts.add(forecast)
                            }
                        }

                        _weeklyForecasts.postValue(allForecasts) // 🔥 Post once only
                        viewModelScope.launch {
                            saveForecastsDaily(weeklyForecasts.value)
                        }
                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_from_web: $it")
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError get_from_web: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun SearchHourlyData(context: Context, city: String) {

        viewModelScope.launch {
            isLoading = true
            try {
                repository.SearchHourlyForecastData(
                    city,
                    onSucess = { rows_hourly ->

                        val cityName = city.substringAfterLast("/")
                        setSavedCity(context, cityName)
                        val allForecasts = mutableListOf<HourlyForecast>()
                        for (row in rows_hourly) {
                            val cells = row.select("td")
                            if (cells.size >= 8) {
                                val forecast = HourlyForecast(
                                    time = cells[0].text(),
                                    temperature = cells[1].text(),
                                    condition = cells[2].text(),
                                    feelsLike = cells[3].text(),
                                    wind = cells[4].text(),
                                    humidity = cells[5].text(),
                                    rainChance = cells[6].text(),
                                    rainAmount = cells[7].text()
                                )
                                allForecasts.add(forecast)
                            }
                        }
                        _hourlyForecasts.postValue(allForecasts) // ✅ Post once after loop
                        viewModelScope.launch {
                            saveForecastsHourly(hourlyForecasts.value)
                        }

                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_hourly_data: $it")
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError GET_HOURLY: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun saveForecastsDaily(daily: List<DailyForecast>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearDaily()
            dao.insertDailyForecast(daily.map { it.toEntity() })
        }
    }

    fun saveForecastsHourly(hourly: List<HourlyForecast>) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.clearHourly()
            dao.insertHourlyForecast(hourly.map { it.toEntity() })

        }
    }

    fun loadForecastsFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val hourly = dao.getHourlyForecast().map { it.toModel() }
            val daily = dao.getDailyForecast().map { it.toModel() }

            withContext(Dispatchers.Main) {
                _hourlyForecasts.value = hourly
                _weeklyForecasts.value = daily
            }
        }
    }

    suspend fun fetchSuggestions(city: String): List<CitySuggestion> = withContext(Dispatchers.IO) {
        try {
            val doc = Jsoup.connect(
                "https://www.timeanddate.com/scripts/completion.php?query=${
                    city.trim().lowercase()
                }&xd=5&mode=ci"
            ).get()

            val raw = doc.body().text()
            val entries = raw.split(" p ")

            Log.d("taxila_entries", entries.toString())

            entries.mapNotNull { entry ->
                val parts = entry.trim().split(" ")
                val id = parts.getOrNull(0) ?: return@mapNotNull null

                // Find index of the image URL (which starts with //c.tadst.com)
                val imageIndex = parts.indexOfFirst { it.startsWith("//c.tadst.com") }
                val nameParts = if (imageIndex != -1) {
                    parts.subList(4, imageIndex)
                } else {
                    parts.drop(4)
                }

                val name = nameParts.joinToString(" ")
                    .replace("(populated place)", "")
                    .trim()

                CitySuggestion(id = id, name = name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun HourlyForecast.toEntity() = HourlyForecast(
        time = time,
        temperature = temperature,
        condition = condition,
        feelsLike = feelsLike,
        wind = wind,
        humidity = humidity,
        rainChance = rainChance,
        rainAmount = rainAmount
    )

    fun HourlyForecast.toModel() = HourlyForecast(
        time = time,
        temperature = temperature,
        condition = condition,
        feelsLike = feelsLike,
        wind = wind,
        humidity = humidity,
        rainChance = rainChance,
        rainAmount = rainAmount
    )

    fun DailyForecast.toEntity() = DailyForecast(
        day = day,
        date = date,
        minTemp = minTemp,
        maxTemp = maxTemp,
        condition = condition,
        feelsLike = feelsLike,
        windSpeed = windSpeed,
        humidity = humidity,
        rainChance = rainChance,
        rainAmount = rainAmount,
        uvIndex = uvIndex,
        sunrise = sunrise,
        sunset = sunset
    )

    fun DailyForecast.toModel() = DailyForecast(
        day = day,
        date = date,
        minTemp = minTemp,
        maxTemp = maxTemp,
        condition = condition,
        feelsLike = feelsLike,
        windSpeed = windSpeed,
        humidity = humidity,
        rainChance = rainChance,
        rainAmount = rainAmount,
        uvIndex = uvIndex,
        sunrise = sunrise,
        sunset = sunset
    )


}
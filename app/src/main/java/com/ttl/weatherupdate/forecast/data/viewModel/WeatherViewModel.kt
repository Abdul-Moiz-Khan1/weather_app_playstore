package com.ttl.weatherupdate.forecast.data.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ttl.weatherupdate.forecast.data.model.DailyForecast
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.ttl.weatherupdate.forecast.data.model.weatherResponse.ApiResponse
import com.ttl.weatherupdate.forecast.data.repository.WeatherRepository
import com.ttl.weatherupdate.forecast.utils.NetworkStatusTracker
import com.ttl.weatherupdate.forecast.utils.Utils.getCountryFromCity
import com.ttl.weatherupdate.forecast.utils.Utils.getLocationName
import org.jsoup.Jsoup
import javax.inject.Inject
import kotlin.toString

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val networkStatusTracker: NetworkStatusTracker
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

    private val _forcast = MutableLiveData<ApiResponse?>()
    val forecast: LiveData<ApiResponse?> = _forcast
    var isLoading by mutableStateOf(false)

    var location_city by mutableStateOf("")
    var location_country by mutableStateOf("")

    var locationPermission by mutableStateOf(false)
    var latitude by mutableStateOf("")
    var longitude by mutableStateOf("")

    fun getDatafromWeb(context: Context, city: String, country: String) {
        if(country == "United States"){
            location_country = "usa"
        }else if (country.contains("united")== true && country.contains("kingdom")){
            location_country = "uk"
        }else if (country.contains(" ")){
            location_country= country.replace(" ", "-")
        }
        if(city.contains(" ")){
            location_city = city.replace(" ", "-")
        }else{
            location_city = city
        }



        viewModelScope.launch {
            isLoading = true
            try {
                repository.getWeeklyDatafromWeb(
                    location_city,
                    location_country,
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
                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_from_web: $it")
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError get_from_web: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun getHourlyData(context: Context, city: String, country: String) {
        if(country == "United States"){
            location_country = "usa"
        }else if (country.contains("united")== true && country.contains("kingdom")){
            location_country = "uk"
        }else if (country.contains(" ")){
            location_country= country.replace(" ", "-")
        }
        if(city.contains(" ")){
            location_city = city.replace(" ", "-")
        }else{
            location_city = city
        }
        viewModelScope.launch {
            isLoading = true
            try {
                repository.getHourlyForecastData(
                    location_city,
                    location_country,
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
                                Log.d("HourlyForecast", forecast.toString())
                            }
                        }

                        _hourlyForecasts.postValue(allForecasts) // ✅ Post once after loop
                    },
                    onError = {
                        Log.d("CatchError_viewmodel", "inError get_hourly_data: $it")
                    }
                )
            } catch (e: Exception) {
                Log.d("CatchError_viewmodel", "inError get_hourly_data: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadForcast(city: String, days: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val data = repository.getForecast(city)
                _forcast.value = data
                Log.d("CatchError,inViewModel,forcast", forecast.value.toString())
                Log.d("CatchError,inViewModel", data.toString())

            } catch (e: Exception) {
                Log.e("eror", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadForecastByLocation(lat: String, lng: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val data = repository.getForecast("$lat,$lng")
                _forcast.value = data
                Log.d("CatchError,inViewModel,forcast", forecast.value.toString())
                Log.d("CatchError,inViewModel", data.toString())

            } catch (e: Exception) {
                Log.e("eror", "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCacheData() {
        viewModelScope.launch {
            isLoading = true
            try {
                val data = repository.getCachedData()
                _forcast.value = data
                Log.d("CatchError_loadcache,inViewModel", data.toString())
            } catch (e: Exception) {
                Log.d("CatchError,inViewModel_loadcache", e.message.toString())
            } finally {
                isLoading = false
            }
        }
    }


}
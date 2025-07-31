package com.ttl.weatherupdate.forecast.data.viewModel

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.ttl.weatherupdate.forecast.data.model.weatherResponse.ApiResponse
import com.ttl.weatherupdate.forecast.data.repository.WeatherRepository
import com.ttl.weatherupdate.forecast.utils.NetworkStatusTracker
import javax.inject.Inject

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

    private val _forcast = MutableLiveData<ApiResponse?>()
    val forecast: LiveData<ApiResponse?> = _forcast
    var isLoading by mutableStateOf(false)

    var locationPermission by mutableStateOf(false)

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
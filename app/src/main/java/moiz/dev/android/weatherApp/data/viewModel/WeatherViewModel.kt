package moiz.dev.android.weatherApp.data.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse
import moiz.dev.android.weatherApp.data.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {
    private val _forcast = MutableLiveData<ApiResponse?>()
    val forecast: LiveData<ApiResponse?> = _forcast

    fun loadForcast(city: String, days: Int) {
        viewModelScope.launch {
            try {
                val data = repository.getForecast(city)
                _forcast.value = data
                Log.d("CatchError,inViewModel,forcast", forecast.value.toString())
                Log.d("CatchError,inViewModel", data.toString())

            } catch (e: Exception) {
                Log.e("eror", "Error: ${e.message}")
            }
        }
    }
    fun loadForecastByLocation(lat: String, lng: String) {
        viewModelScope.launch {
            try {
                val data = repository.getForecast("$lat,$lng")
                _forcast.value = data
                Log.d("CatchError,inViewModel,forcast", forecast.value.toString())
                Log.d("CatchError,inViewModel", data.toString())

            } catch (e: Exception) {
                Log.e("eror", "Error: ${e.message}")
            }
        }
    }

    fun loadCacheData() {
        viewModelScope.launch {
            try {
                val data = repository.getCachedData()
                _forcast.value = data
                Log.d("CatchError_loadcache,inViewModel", data.toString())
            } catch (e: Exception) {
                Log.d("CatchError,inViewModel_loadcache", e.message.toString())
            }
        }
    }

}
package com.ttl.weatherupdate.forecast.data.repository

import android.util.Log
import com.ttl.weatherupdate.forecast.data.local.WeatherDao
import com.ttl.weatherupdate.forecast.data.model.weatherResponse.ApiResponse
import com.ttl.weatherupdate.forecast.data.remote.ApiInterface
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: ApiInterface,
    private val dao: WeatherDao
) {
    suspend fun getForecast(city: String): ApiResponse? {
            try {
                Log.d("CatchError_repo,intry", "try")
                val response = api.getForecast(city, "metric", "NTG86NK4QTHXDTMVDA85KCQ6A", "json")
                Log.d("CatchError_repo,intry", response.toString())
                dao.insertWeather(response)
                return response
            } catch (e: Exception) {

            Log.d("CatchError_repo,inCatch", e.message.toString())
            return getCachedData()
        }
    }

    suspend fun getCachedData(): ApiResponse? {
        val response = dao.getCacheWeather()
        return response
    }
}
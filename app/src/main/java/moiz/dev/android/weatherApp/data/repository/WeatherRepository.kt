package moiz.dev.android.weatherApp.data.repository

import android.util.Log
import moiz.dev.android.weatherApp.data.local.WeatherDao
import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse
import moiz.dev.android.weatherApp.data.remote.ApiInterface
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: ApiInterface,
    private val dao: WeatherDao
) {
    suspend fun getForecast(city: String , connecton:(Boolean) -> Unit): ApiResponse? {
        try {
            val response = api.getForecast(city, "metric", "NTG86NK4QTHXDTMVDA85KCQ6A", "json")
            Log.d("CatchError_repo,intry", response.toString())
            dao.insertWeather(response)
            return response
        } catch (e: Exception) {
            connecton(false)
            Log.d("CatchError_repo,inCatch", e.message.toString())
            return getCachedData()

        }
    }

    suspend fun getCachedData(): ApiResponse? {
        val response = dao.getCacheWeather()
        return response
    }
}
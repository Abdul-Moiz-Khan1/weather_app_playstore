package com.ttl.weatherupdate.forecast.data.repository

import android.util.Log
import com.ttl.weatherupdate.forecast.data.local.WeatherDao
import com.ttl.weatherupdate.forecast.data.model.weatherResponse.ApiResponse
import com.ttl.weatherupdate.forecast.data.remote.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.select.Elements

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

    suspend fun getWeeklyDatafromWeb(
        city: String,
        country: String,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val doc =
                Jsoup.connect("https://www.timeanddate.com/weather/${country}/${city}/ext")
                    .get()
            val rows = doc.select("table.zebra.tb-wt.fw.va-m tbody tr")
            onSucess(rows)
        } catch (e: Exception) {
            onError(e.message.toString())
            Log.d("CatchError_repo_fromweb", "inCatch: ${e.message}")
        }
    }

    suspend fun getHourlyForecastData(
        location_city: String,
        location_country: String,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    )= withContext(Dispatchers.IO) {
        try {
            val doc_hourly =
                Jsoup.connect("https://www.timeanddate.com/weather/${location_country}/${location_city}/hourly")
                    .get()
            val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
            onSucess(rows_hourly)
        } catch (e: Exception) {
            Log.d("CatchError_repo", "inCatch_hourlyforecast: ${e.message}")
            onError(e.message.toString())
        }
    }

}
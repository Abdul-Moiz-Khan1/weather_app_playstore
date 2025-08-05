package com.ttl.weatherupdate.forecast.data.repository

import android.content.Context
import android.util.Log
import com.ttl.weatherupdate.forecast.data.local.WeatherDao
import com.ttl.weatherupdate.forecast.data.model.DailyForecast
import com.ttl.weatherupdate.forecast.data.model.HourlyForecast
import com.ttl.weatherupdate.forecast.utils.Utils.setSavedCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class WeatherRepository @Inject constructor(
    private val dao: WeatherDao
) {


    suspend fun getWeeklyDatafromWeb(
        city: String,
        country: String,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            Log.d("weeklyyURL ", "https://www.timeanddate.com/weather/${country}/${city}/ext")

            if (city.contains("taxila", ignoreCase = true)) {
                val doc =
                    Jsoup.connect("https://www.timeanddate.com/weather/@11785970/ext")
                        .get()

                val rows = doc.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows)
            } else {
                val doc =
                    Jsoup.connect("https://www.timeanddate.com/weather/${country}/${city}/ext")
                        .get()

                val rows = doc.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows)
            }


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
    ) = withContext(Dispatchers.IO) {
        try {

            if (location_city.contains("taxila", ignoreCase = true)) {
                Log.d(
                    "hourlyURL ",
                    "https://www.timeanddate.com/weather/@11785970/hourly"
                )

                val doc_hourly =
                    Jsoup.connect("https://www.timeanddate.com/weather/@11785970/hourly")
                        .get()
                val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows_hourly)
            } else {
                Log.d(
                    "hourlyURL ",
                    "https://www.timeanddate.com/weather/${location_country}/${location_city}/hourly"
                )

                val doc_hourly =
                    Jsoup.connect("https://www.timeanddate.com/weather/${location_country}/${location_city}/hourly")
                        .get()
                val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows_hourly)
            }

        } catch (e: Exception) {
            Log.d("CatchError_repo", "inCatch_hourlyforecast: ${e.message}")
            onError(e.message.toString())
        }
    }


}
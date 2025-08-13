package com.ttl.weatherupdate.forecast.data.repository

import android.content.Context
import android.util.Log
import com.ttl.weatherupdate.forecast.data.local.WeatherDao
import com.ttl.weatherupdate.forecast.utils.Utils.setSavedCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import javax.inject.Inject
import org.jsoup.select.Elements

class WeatherRepository @Inject constructor(
    private val dao: WeatherDao
) { 
    suspend fun getWeeklyDatafromWeb(
        context: Context,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient.Builder()
                .followRedirects(false)  // Important!
                .build()

            val request = Request.Builder()
                .url("https://www.timeanddate.com/scripts/go.php")
                .header("User-Agent", "Mozilla/5.0") // Some sites require it
                .build()

            val response = client.newCall(request).execute()

            if (response.isRedirect) {
                val redirectedUrl = response.header("Location")  // ← this is the final URL
                Log.d("Redirected URL", redirectedUrl.toString())
                val city = redirectedUrl?.substringAfterLast("/")
                setSavedCity(context, city.toString())

                val doc_hourly =
                    Jsoup.connect("$redirectedUrl/ext")
                        .get()
                val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows_hourly)
            }


        } catch (e: Exception) {
            Log.d("CatchError_repo", "inCatch_hourlyforecast: ${e.message}")
            onError(e.message.toString())
        }
    }

    suspend fun getHourlyForecastData(
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {

        try {

            val client = OkHttpClient.Builder()
                .followRedirects(false)  // Important!
                .build()

            val request = Request.Builder()
                .url("https://www.timeanddate.com/scripts/go.php")
                .header("User-Agent", "Mozilla/5.0") // Some sites require it
                .build()

            val response = client.newCall(request).execute()

            if (response.isRedirect) {
                val redirectedUrl = response.header("Location")  // ← this is the final URL
                Log.d("Redirected URL", redirectedUrl.toString())

                val doc_hourly =
                    Jsoup.connect("$redirectedUrl/hourly")
                        .get()
                val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows_hourly)
            }


        } catch (e: Exception) {
            Log.d("CatchError_repo", "inCatch_hourlyforecast: ${e.message}")
            onError(e.message.toString())
        }
    }

    suspend fun SearchWeeklyDatafromWeb(
        city: String,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {

            Log.d(
                "hourlyURL ",
                "https://www.timeanddate.com${city}/ext"
            )

            val doc =
                Jsoup.connect("https://www.timeanddate.com${city}/ext")
                    .get()
            val rows = doc.select("table.zebra.tb-wt.fw.va-m tbody tr")
            onSucess(rows)


        } catch (e: Exception) {
            onError(e.message.toString())
            Log.d("CatchError_repo_fromweb", "inCatch: ${e.message}")
        }
    }

    suspend fun SearchHourlyForecastData(
        location_city: String,
        onSucess: (Elements) -> Unit,
        onError: (String) -> Unit
    ) = withContext(Dispatchers.IO) {

        try {


                Log.d(
                    "hourlyURL ",
                    "https://www.timeanddate.com${location_city}/hourly"
                )

                val doc_hourly =
                    Jsoup.connect( "https://www.timeanddate.com${location_city}/hourly")
                        .get()
                val rows_hourly = doc_hourly.select("table.zebra.tb-wt.fw.va-m tbody tr")
                onSucess(rows_hourly)


        } catch (e: Exception) {
            Log.d("CatchError_repo", "inCatch_hourlyforecast: ${e.message}")
            onError(e.message.toString())
        }
    }

}
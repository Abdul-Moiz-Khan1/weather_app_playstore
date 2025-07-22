package moiz.dev.android.weatherApp.data.remote

import moiz.dev.android.weatherApp.data.model.response.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int
    )   : Weather

}
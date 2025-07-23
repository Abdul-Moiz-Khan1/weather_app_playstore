package moiz.dev.android.weatherApp.data.remote

import moiz.dev.android.weatherApp.data.model.weatherResponse.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("VisualCrossingWebServices/rest/services/timeline/{location}/next7days")
    suspend fun getForecast(
        @Path("location") location: String,
        @Query("unitGroup") unitGroup: String = "metric",
        @Query("key") apiKey: String,
        @Query("contentType") contentType: String = "json"
    )   : ApiResponse

}
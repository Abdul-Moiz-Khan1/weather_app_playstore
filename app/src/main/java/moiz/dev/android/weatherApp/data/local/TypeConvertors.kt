package moiz.dev.android.weatherApp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import moiz.dev.android.weatherApp.data.model.weatherResponse.CurrentConditions
import moiz.dev.android.weatherApp.data.model.weatherResponse.Day
import moiz.dev.android.weatherApp.data.model.weatherResponse.Stations

class WeatherTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromCurrentConditions(value: CurrentConditions): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurrentConditions(value: String): CurrentConditions {
        return gson.fromJson(value, CurrentConditions::class.java)
    }

    @TypeConverter
    fun fromDayList(value: List<Day>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toDayList(value: String): List<Day> {
        val type = object : TypeToken<List<Day>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStations(value: Stations): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStations(value: String): Stations {
        return gson.fromJson(value, Stations::class.java)
    }

    @TypeConverter
    fun fromAnyList(value: List<Any>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toAnyList(value: String): List<Any> {
        val type = object : TypeToken<List<Any>>() {}.type
        return gson.fromJson(value, type)
    }
}
package com.ahmetefeozenc.weatherapp.Repository

import com.ahmetefeozenc.weatherapp.Model.WeatherResponse
import com.ahmetefeozenc.weatherapp.Utils.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    private val weatherApiService = RetrofitClient.weatherApiService

    suspend fun getWeatherData(city: String): WeatherResponse {
        return withContext(Dispatchers.IO) {
            weatherApiService.getWeatherData(
                city = city,
                unitGroup = "metric",
                include = "days",
                key = "HWWQRQBHQLHQ2NVALDDY4GJP9",
                contentType = "json"
            )
        }
    }
}

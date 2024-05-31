package com.ahmetefeozenc.weatherapp.Data

import com.ahmetefeozenc.weatherapp.Model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {
    @GET("VisualCrossingWebServices/rest/services/timeline/{city}")
    suspend fun getWeatherData(
        @Path("city") city: String,
        @Query("unitGroup") unitGroup: String,
        @Query("include") include: String,
        @Query("key") key: String,
        @Query("contentType") contentType: String
    ): WeatherResponse
}

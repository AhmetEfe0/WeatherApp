package com.ahmetefeozenc.weatherapp.Utils

import com.ahmetefeozenc.weatherapp.Data.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://weather.visualcrossing.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherApiService: WeatherApiService = retrofit.create(WeatherApiService::class.java)
}
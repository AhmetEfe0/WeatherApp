package com.ahmetefeozenc.weatherapp.Model

data class WeatherCard(
    val temperature: Double,
    val tempmax: Double,
    val tempmin: Double,
    val humidity: Double,
    val windspeed: Double,
    val icon: String,
    val date: String
)

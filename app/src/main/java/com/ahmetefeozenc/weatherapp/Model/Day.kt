package com.ahmetefeozenc.weatherapp.Model

data class Day(
    val datetime:String,
    val temp: Double,
    val tempmax: Double,
    val tempmin: Double,
    val humidity: Double,
    val windspeed: Double,
    val icon: String
)

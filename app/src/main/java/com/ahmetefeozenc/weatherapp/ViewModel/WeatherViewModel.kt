package com.ahmetefeozenc.weatherapp.ViewModel

import androidx.lifecycle.*
import com.ahmetefeozenc.weatherapp.Model.Day
import com.ahmetefeozenc.weatherapp.Model.WeatherCard
import com.ahmetefeozenc.weatherapp.Repository.WeatherRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherList = MutableLiveData<List<WeatherCard>>()
    val weatherList: LiveData<List<WeatherCard>> get() = _weatherList

    private val _mainWeather = MutableLiveData<Day>()
    val mainWeather: LiveData<Day> get() = _mainWeather

    private val _isRefreshing = MutableLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _isDataLoaded = MutableLiveData<Boolean>()
    val isDataLoaded: LiveData<Boolean> get() = _isDataLoaded

    init {
        _isRefreshing.value = false
        _isDataLoaded.value = false
    }

    fun fetchWeatherData(city: String) {
        _isRefreshing.value = true
        viewModelScope.launch {
            try {
                val response = repository.getWeatherData(city)
                _mainWeather.value = response.days.firstOrNull()
                _weatherList.value = response.days.map { day ->
                    WeatherCard(
                        day.temp,
                        day.tempmax,
                        day.tempmin,
                        day.humidity,
                        day.windspeed,
                        day.icon,
                        formatDate(day.datetime)
                    )
                }
                _isDataLoaded.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _isDataLoaded.value = false
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }
}
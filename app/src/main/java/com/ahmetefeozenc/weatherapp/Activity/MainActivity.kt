package com.ahmetefeozenc.weatherapp.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ahmetefeozenc.weatherapp.Adapter.WeatherCardAdapter
import com.ahmetefeozenc.weatherapp.Activity.LoadingActivity
import com.ahmetefeozenc.weatherapp.Model.Day
import com.ahmetefeozenc.weatherapp.databinding.ActivityMainBinding
import com.ahmetefeozenc.weatherapp.Model.WeatherCard
import com.ahmetefeozenc.weatherapp.R
import com.ahmetefeozenc.weatherapp.Repository.WeatherRepository
import com.ahmetefeozenc.weatherapp.Utils.NetworkUtils
import com.ahmetefeozenc.weatherapp.ViewModel.WeatherViewModel
import com.ahmetefeozenc.weatherapp.ViewModel.WeatherViewModelFactory
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var tarih: TextView
    private lateinit var durum: TextView
    private lateinit var derece: TextView
    private lateinit var maxderece: TextView
    private lateinit var minderece: TextView
    private lateinit var ruzgar: TextView
    private lateinit var nem: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherCardAdapter: WeatherCardAdapter

    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(WeatherRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!NetworkUtils.isNetworkAvailable(this)) {
            // İnternet yoksa yükleme ekranını göster
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupUI()
        observeViewModel()
        weatherViewModel.fetchWeatherData("İstanbul")

        swipeRefreshLayout.setOnRefreshListener {
            if (NetworkUtils.isNetworkAvailable(this)) {
                weatherViewModel.fetchWeatherData("İstanbul")
            } else {
                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(binding.root, "İnternet bağlantısı yok", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupUI() {
        swipeRefreshLayout = binding.swipeRefreshLayout
        tarih = binding.tarih
        durum = binding.durum
        derece = binding.derece
        maxderece = binding.maxderece
        minderece = binding.minderece
        ruzgar = binding.ruzgar
        nem = binding.nem

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        weatherCardAdapter = WeatherCardAdapter(emptyList()) { weatherCard ->
            displayWeatherCardDetails(weatherCard)
        }
        recyclerView.adapter = weatherCardAdapter
    }

    private fun observeViewModel() {
        weatherViewModel.mainWeather.observe(this) { day ->
            day?.let {
                updateMainWeather(it)
            }
        }
        weatherViewModel.weatherList.observe(this) { weatherList ->
            weatherCardAdapter.updateData(weatherList)
        }
        weatherViewModel.isRefreshing.observe(this) { isRefreshing ->
            swipeRefreshLayout.isRefreshing = isRefreshing
        }
    }

    private fun updateMainWeather(day: Day) {
        tarih.text = weatherViewModel.formatDate(day.datetime)
        durum.text = getConditionText(day.icon)
        derece.text = "${day.temp}°C"
        maxderece.text = "${day.tempmax}°C"
        minderece.text = "${day.tempmin}°C"
        nem.text = "${day.humidity}%"
        ruzgar.text = "${day.windspeed} km/h"
        updateBackground(day.icon)
    }

    private fun displayWeatherCardDetails(weatherCard: WeatherCard) {
        tarih.text = weatherCard.date
        durum.text = getConditionText(weatherCard.icon)
        derece.text = "${weatherCard.temperature}°C"
        maxderece.text = "${weatherCard.tempmax}°C"
        minderece.text = "${weatherCard.tempmin}°C"
        nem.text = "${weatherCard.humidity}%"
        ruzgar.text = "${weatherCard.windspeed} km/h"
        updateBackground(weatherCard.icon)
    }

    private fun getConditionText(icon: String): String {
        return when (icon) {
            "partly-cloudy-day" -> "Bulutlu"
            "clear-day" -> "Güneşli"
            "rain" -> "Yağmurlu"
            else -> "Bilinmeyen"
        }
    }

    private fun updateBackground(icon: String) {
        val backgroundResource = when (icon) {
            "clear-day" -> R.drawable.sunny
            "rain" -> R.drawable.rainy
            "partly-cloudy-day" -> R.drawable.cloudy
            else -> R.drawable.cloudy
        }
        binding.root.setBackgroundResource(backgroundResource)
    }
}
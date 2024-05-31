package com.ahmetefeozenc.weatherapp.Activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ahmetefeozenc.weatherapp.Repository.WeatherRepository
import com.ahmetefeozenc.weatherapp.Utils.NetworkUtils
import com.ahmetefeozenc.weatherapp.ViewModel.WeatherViewModel
import com.ahmetefeozenc.weatherapp.ViewModel.WeatherViewModelFactory
import com.ahmetefeozenc.weatherapp.databinding.ActivityLoadingBinding

class LoadingActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoadingBinding
    private lateinit var networkReceiver: BroadcastReceiver
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(WeatherRepository())
    }
    private lateinit var handler: Handler
    private val timeout: Long = 8000 // 8 saniye

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handler = Handler(Looper.getMainLooper())

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (NetworkUtils.isNetworkAvailable(context)) {
                    weatherViewModel.fetchWeatherData("İstanbul")
                    observeViewModel()
                }
            }
        }

        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

        handler.postDelayed({
            if (!weatherViewModel.isDataLoaded.value!!) {
                binding.textView.text="Lütfen internet bağlantınızı kontrol edin"
            }
        }, timeout)
    }

    private fun observeViewModel() {
        weatherViewModel.isDataLoaded.observe(this) { isDataLoaded ->
            if (isDataLoaded) {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
        handler.removeCallbacksAndMessages(null)
    }
}
package com.ahmetefeozenc.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.weatherapp.Model.WeatherCard
import com.ahmetefeozenc.weatherapp.R

class WeatherCardAdapter(
    private var weatherList: List<WeatherCard>,
    private val onItemClick: (WeatherCard) -> Unit
) : RecyclerView.Adapter<WeatherCardAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val temperature: TextView = view.findViewById(R.id.derece)
        val icon: ImageView = view.findViewById(R.id.icon)
        val date: TextView = view.findViewById(R.id.tarih)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recylerview_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherCard = weatherList[position]
        holder.temperature.text = "${weatherCard.temperature}°C"
        holder.icon.setImageResource(getIconResource(weatherCard.icon))
        holder.date.text = weatherCard.date
        holder.itemView.setOnClickListener {
            onItemClick(weatherCard)
        }
    }

    override fun getItemCount() = weatherList.size

    fun updateData(newWeatherList: List<WeatherCard>) {
        weatherList = newWeatherList
        notifyDataSetChanged()
    }

    private fun getIconResource(icon: String): Int {
        return when (icon) {
            "partly-cloudy-day" -> R.drawable.baseline_cloud_24
            "clear-day" -> R.drawable.baseline_wb_sunny_24
            "rain" -> R.drawable.baseline_bolt_24
            else -> R.drawable.baseline_cloud_24
        }
    }
}
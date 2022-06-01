package com.brant.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brant.weatherapp.R
import com.brant.weatherapp.data.model.Hourly
import com.brant.weatherapp.utils.unixToDateTime
import kotlinx.android.synthetic.main.list_item_hourly.view.*
import kotlin.math.roundToInt

class HourlyForecastAdapter(
  private val hourlyForecast: List<Hourly>,
  private val timezone: String,
) : RecyclerView.Adapter<HourlyForecastAdapter.HourlyForecastViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_hourly, parent, false)
    return HourlyForecastViewHolder(view, timezone)
  }

  override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
    holder.bind(hourlyForecast[position])
  }

  override fun getItemCount() = hourlyForecast.size

  class HourlyForecastViewHolder(view: View, private val timezone: String) : RecyclerView.ViewHolder(view) {
    fun bind(hourly: Hourly) {
      val time = hourly.dt.unixToDateTime(timezone)
      val temp = "${hourly.temp.roundToInt()}°F"
      val pop = "${hourly.pop.times(100).roundToInt()}% chance of rain"
      itemView.time.text = time
      itemView.temp.text = temp
      itemView.precipitation.text = pop
    }
  }
}
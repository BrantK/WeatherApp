package com.brant.weatherapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brant.weatherapp.R
import com.brant.weatherapp.data.model.City
import kotlinx.android.synthetic.main.list_item_city.view.*
import kotlin.math.roundToInt

class CityListAdapter(private val clickListener: (city: City) -> Unit) :
  RecyclerView.Adapter<CityListAdapter.CityListViewHolder>() {

  private var cityList = emptyList<City>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_city, parent, false)
    return CityListViewHolder(view, clickListener)
  }

  override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
    holder.bind(cityList[position])
  }

  override fun getItemCount() = cityList.size

  @SuppressLint("NotifyDataSetChanged")
  fun updateList(cityList: List<City>) {
    this.cityList = cityList
    //Should use DiffUtil instead
    notifyDataSetChanged()
  }

  class CityListViewHolder(view: View, private val clickListener: (city: City) -> Unit) :
    RecyclerView.ViewHolder(view) {
    fun bind(city: City) {
      val currentTemp = "${city.weatherData?.currentTemp?.temp?.roundToInt()}°F"
      val description = city.weatherData?.currentTemp?.weather?.get(0)?.description

      itemView.container.setOnClickListener { clickListener.invoke(city) }
      itemView.city_name.text = city.name
      itemView.city_temp.text = currentTemp
      itemView.city_temp_description.text = description
    }
  }
}
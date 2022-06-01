package com.brant.weatherapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.brant.weatherapp.R
import com.brant.weatherapp.data.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_current_weather.*
import kotlin.math.roundToInt

class CurrentWeatherFragment : Fragment(R.layout.fragment_current_weather) {

  private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ViewModelProvider(this)[MainViewModel::class.java]
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.toolbar_title?.text = getString(R.string.title_current_forecast)
    viewModel.selectedCity.observe(requireActivity()) { city ->
      city?.weatherData?.currentTemp?.let {
        val currentTemp = "${it.temp.roundToInt()}°F"
        val tempDescription = it.weather[0].main
        val feelsLike = "Feels like ${it.feelsLike.roundToInt()}°F"
        val humidity = "${it.humidity}% humidity"

        city_name?.text = city.name
        temp?.text = currentTemp
        description?.text = tempDescription
        feels_like?.text = feelsLike
        humidity_text?.text = humidity
      }
    }
  }
}
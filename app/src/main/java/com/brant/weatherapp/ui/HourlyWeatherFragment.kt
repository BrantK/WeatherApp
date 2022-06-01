package com.brant.weatherapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.brant.weatherapp.R
import com.brant.weatherapp.data.viewmodel.MainViewModel
import com.brant.weatherapp.ui.adapter.HourlyForecastAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_hourly_weather.*

class HourlyWeatherFragment : Fragment(R.layout.fragment_hourly_weather) {

  private val viewModel: MainViewModel by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    ViewModelProvider(this)[MainViewModel::class.java]
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.toolbar_title?.text = getString(R.string.title_hourly_forecast)
    viewModel.selectedCity.observe(requireActivity()) { city ->
      city?.weatherData?.hourlyTemp?.let {
        val timezone = city.weatherData?.timezone ?: ""
        hourly_recycler_view?.adapter = HourlyForecastAdapter(it, timezone)
        hourly_recycler_view?.layoutManager = LinearLayoutManager(requireContext())
      }
    }
  }

  override fun onPause() {
    super.onPause()
    //Need to remove the observer on pause because switching tabs can throw a npe
    viewModel.selectedCity.removeObservers(requireActivity())
  }
}
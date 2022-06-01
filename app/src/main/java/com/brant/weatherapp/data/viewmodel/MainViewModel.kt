package com.brant.weatherapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brant.weatherapp.data.model.City
import com.brant.weatherapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel constructor(application: Application): AndroidViewModel(application) {

  private val repo = MainRepository.getInstance(application)
  val citiesMap = repo.citiesMap
  val selectedCity = repo.selectedCity
  val state = repo.state

  //Gets weather data for the specified city then adds it to the CityList
  fun getWeatherDataAndAddCity(city: City) {
    state.postValue(MainRepository.State.LOADING)
    viewModelScope.launch {
      val weatherData = repo.getWeatherForCity(city)
      city.weatherData = weatherData
      repo.addCityToList(city)
      repo.setSelectedCity(city)
    }
  }

  //Refreshes the weather data for the currently selected city
  fun refreshWeatherData() {
    val selectedCity = selectedCity.value
    selectedCity?.let {
      viewModelScope.launch {
        val weatherData = repo.getWeatherForCity(it)
        it.weatherData = weatherData
        repo.addCityToList(it)
        repo.setSelectedCity(it)
      }
    }
  }

  //Removes the specified city from the city list
  fun removeCityFromList(city: City) {
    repo.removeCityFromList(city)
  }

  //Sets the specified city to show a detailed forecast
  fun setSelectedCity(city: City) {
    repo.setSelectedCity(city)
  }

  //Returns a boolean based on if a city has been selected or not
  fun selectedCityAvailable(): Boolean {
    return repo.getSelectedCity() != null
  }
}
package com.brant.weatherapp.data.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.brant.weatherapp.data.model.City
import com.brant.weatherapp.data.model.WeatherData
import com.brant.weatherapp.network.NetworkManager
import com.brant.weatherapp.utils.toJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainRepository constructor(application: Application) {

  //Using shared prefs to store values to disk, if I had more time I'd use room or realm
  private val sharedPrefs = application.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)

  //A map of cities that the user has created
  val citiesMap = MutableLiveData<Map<String, City>>()

  //This is the city that is currently displaying a detailed forecast
  val selectedCity = MutableLiveData<City?>()

  //The state the app is currently in
  val state = MutableLiveData<State>()

  enum class State {
    LOADING,
    SUCCESS,
    ERROR
  }

  init {
    //Update currently selected city to the stored city from last session
    val selectedCityJson = sharedPrefs.getString(SHARED_PREFS_SELECTED_CITY, null)
    val cityType = object : TypeToken<City>() {}.type
    Gson().fromJson<City>(selectedCityJson, cityType)?.let {
      selectedCity.postValue(it)
    }

    //Update city list live data with stored list if we have it
    val cityListJson = sharedPrefs.getString(SHARED_PREFS_CITY_LIST, null)
    val citiesMapType = object : TypeToken<Map<String, City>>() {}.type
    Gson().fromJson<Map<String, City>>(cityListJson, citiesMapType)?.let {
      citiesMap.postValue(it)
    }
  }

  //Gets weather data from the weather api and returns a WeatherData object
  suspend fun getWeatherForCity(city: City): WeatherData? {
    return NetworkManager.api.getWeather(city.lat, city.lon).let { response ->
      if (response.isSuccessful) {
        Log.i(TAG, "getWeather success")
        state.postValue(State.SUCCESS)
        response.body()
      } else {
        Log.e(TAG, "getWeather error: ${response.code()} - ${response.errorBody()?.toString()}")
        state.postValue(State.ERROR)
        null
      }
    }
  }

  fun addCityToList(city: City) {
    val updatedMap = citiesMap.value?.toMutableMap() ?: mutableMapOf()
    updatedMap[city.name] = city
    sharedPrefs.edit().putString(SHARED_PREFS_CITY_LIST, updatedMap.toJson()).apply()
    citiesMap.postValue(updatedMap)
  }

  fun removeCityFromList(city: City) {
    val updatedMap = citiesMap.value?.toMutableMap() ?: mutableMapOf()
    updatedMap.remove(city.name)
    sharedPrefs.edit().putString(SHARED_PREFS_CITY_LIST, updatedMap.toJson()).apply()
    citiesMap.postValue(updatedMap)
    if (selectedCity.value?.name == city.name) {
      sharedPrefs.edit().remove(SHARED_PREFS_SELECTED_CITY).apply()
      selectedCity.value = null
    }
  }

  fun setSelectedCity(city: City) {
    selectedCity.postValue(city)
    sharedPrefs.edit().putString(SHARED_PREFS_SELECTED_CITY, city.toJson()).apply()
  }

  fun getSelectedCity(): String? {
    return sharedPrefs.getString(SHARED_PREFS_SELECTED_CITY, null)
  }

  companion object {
    private const val TAG = "MainRepository"
    private const val SHARED_PREFS_KEY = "sharedPrefsKey"
    private const val SHARED_PREFS_SELECTED_CITY = "selectedCity"
    private const val SHARED_PREFS_CITY_LIST = "cityList"
    private var internalInstance: MainRepository? = null

    //It would be better to use dependency injection but this gets the job done
    fun getInstance(application: Application): MainRepository {
      if (internalInstance == null) {
        internalInstance = MainRepository(application)
      }
      return internalInstance!!
    }
  }
}
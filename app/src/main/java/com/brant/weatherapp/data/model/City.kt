package com.brant.weatherapp.data.model

data class City(
  val name: String,
  val lat: Double,
  val lon: Double
) {
  var weatherData: WeatherData? = null

  override fun equals(other: Any?): Boolean {
    return other != null && other is City && other.name == name
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }
}
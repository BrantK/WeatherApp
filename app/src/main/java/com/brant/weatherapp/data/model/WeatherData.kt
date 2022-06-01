package com.brant.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
  @SerializedName("lat")
  val latitude: Double = 0.0,
  @SerializedName("lon")
  val longitude: Double = 0.0,
  @SerializedName("timezone")
  val timezone: String? = null,
  @SerializedName("timezone_offset")
  val timezoneOffset: Long = 0,
  @SerializedName("current")
  val currentTemp: Current? = Current(),
  @SerializedName("hourly")
  val hourlyTemp: List<Hourly> = listOf(),
)

data class Weather(
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("main")
  val main: String? = null,
  @SerializedName("description")
  val description: String? = null,
  @SerializedName("icon")
  val icon: String? = null,
)

data class Current(
  @SerializedName("dt")
  val dt: Long = 0,
  @SerializedName("sunrise")
  val sunrise: Long = 0,
  @SerializedName("sunset")
  val sunset: Long = 0,
  @SerializedName("temp")
  val temp: Double = 0.0,
  @SerializedName("feels_like")
  val feelsLike: Double = 0.0,
  @SerializedName("pressure")
  val pressure: Int = 0,
  @SerializedName("humidity")
  val humidity: Int = 0,
  @SerializedName("dew_point")
  val dewPoint: Double = 0.0,
  @SerializedName("uvi")
  val uvi: Double = 0.0,
  @SerializedName("clouds")
  val clouds: Int = 0,
  @SerializedName("visibility")
  val visibility: Int = 0,
  @SerializedName("wind_speed")
  val windSpeed: Double = 0.0,
  @SerializedName("wind_deg")
  val windDeg: Int = 0,
  @SerializedName("wind_gust")
  val windGust: Double = 0.0,
  @SerializedName("weather")
  val weather: List<Weather> = listOf()
)

data class Hourly(
  @SerializedName("dt")
  val dt: Long = 0,
  @SerializedName("temp")
  val temp: Double = 0.0,
  @SerializedName("feels_like")
  val feelsLike: Double = 0.0,
  @SerializedName("pressure")
  val pressure: Int = 0,
  @SerializedName("humidity")
  val humidity: Int = 0,
  @SerializedName("dew_point")
  val dewPoint: Double = 0.0,
  @SerializedName("uvi")
  val uvi: Double = 0.0,
  @SerializedName("clouds")
  val clouds: Int = 0,
  @SerializedName("visibility")
  val visibility: Int = 0,
  @SerializedName("wind_speed")
  val windSpeed: Double = 0.0,
  @SerializedName("wind_deg")
  val windDeg: Int = 0,
  @SerializedName("wind_gust")
  val windGust: Double = 0.0,
  @SerializedName("weather")
  val weather: List<Weather> = listOf(),
  @SerializedName("pop")
  val pop: Double = 0.0
)
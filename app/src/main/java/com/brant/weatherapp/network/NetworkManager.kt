package com.brant.weatherapp.network

import android.util.Log
import com.brant.weatherapp.data.model.WeatherData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object NetworkManager {

  private const val apiKey = "378509d880ae5ad20333cc0b2a3b2dee"
  private const val TAG = "NetworkManager"
  private const val baseUrl = "https://api.openweathermap.org/data/2.5/"
  private val logger = HttpLoggingInterceptor.Logger { Log.i(TAG, it) }

  private val loggingInterceptor = HttpLoggingInterceptor(logger).apply {
    level = HttpLoggingInterceptor.Level.BASIC
  }

  private val baseInterceptor = Interceptor {
    val requestUrl = it.request().url.newBuilder().build()
    val request = it.request().newBuilder().url(requestUrl).build()
    it.proceed(request)
  }

  private val okHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(baseInterceptor)
    .build()

  private fun retrofit(): Retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

  val api: NetworkApi = retrofit().create(NetworkApi::class.java)

  interface NetworkApi {
    @GET("onecall?appid=$apiKey&units=imperial")
    suspend fun getWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): Response<WeatherData>
  }
}
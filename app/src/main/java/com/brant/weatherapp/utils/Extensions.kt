package com.brant.weatherapp.utils

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

fun Any.toJson(): String {
  return Gson().toJson(this)
}

fun Long.unixToDateTime(timeZoneId: String): String {
  return SimpleDateFormat("h a", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone(timeZoneId)
  }.format(Date(this * 1000)).lowercase()
}
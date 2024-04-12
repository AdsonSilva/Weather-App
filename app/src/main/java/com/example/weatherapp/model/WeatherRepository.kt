package com.example.weatherapp.model

import com.example.weatherapp.BuildConfig




class WeatherRepository(private val weatherService: WeatherService) {

    suspend fun getWeather(query: String) = weatherService.getWeather(BuildConfig.API_KEY, query)
}
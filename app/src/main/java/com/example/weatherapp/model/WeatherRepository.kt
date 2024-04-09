package com.example.weatherapp.model

class WeatherRepository(private val weatherService: WatherService) {

    suspend fun getWeather() = weatherService.getWeather()
}
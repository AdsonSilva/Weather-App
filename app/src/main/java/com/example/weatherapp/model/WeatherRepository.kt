package com.example.weatherapp.model

class WeatherRepository(private val weatherService: WeatherService) {

    suspend fun getWeather() = weatherService.getWeather("c2e048ffb0a4932392398024a95cc6b3", "Joao Pessoa")
}
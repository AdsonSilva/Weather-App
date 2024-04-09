package com.example.weatherapp.model

import com.example.weatherapp.model.dtos.WeatherDTO
import retrofit2.http.Headers
import retrofit2.http.GET


interface WatherService {

    @Headers("Accept: application/json")
    @GET("")
    suspend fun getWeather(): WeatherDTO
}
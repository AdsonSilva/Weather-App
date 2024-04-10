package com.example.weatherapp.model

import com.example.weatherapp.model.dtos.WeatherDTO
import retrofit2.http.Headers
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {

    @Headers("Accept: application/json")
    @GET("current")
    suspend fun getWeather(
        @Query("access_key") accessKey: String,
        @Query("query") query: String
    ): WeatherDTO
}
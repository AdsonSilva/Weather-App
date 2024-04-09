package com.example.weatherapp.model.dtos

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("request") val request: RequestDataDTO,
    @SerializedName("location") val location: LocationDTO,
    @SerializedName("current") val current: CurrentDTO
)
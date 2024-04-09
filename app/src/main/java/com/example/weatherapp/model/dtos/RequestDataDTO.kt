package com.example.weatherapp.model.dtos

import com.google.gson.annotations.SerializedName

data class RequestDataDTO(
    @SerializedName("type") val type: RequestType,
    @SerializedName("query") val query: String,
    @SerializedName("language") val language: String,
    @SerializedName("unit") val unit: Unit
)
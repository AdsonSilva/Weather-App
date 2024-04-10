package com.example.weatherapp.model.dtos

import com.google.gson.annotations.SerializedName
import java.util.Date

data class LocationDTO(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("region") val region: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("timezone_id") val timezoneId: String,
    @SerializedName("localtime") val localtime: Date,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    @SerializedName("utc_offset") val utcOffset: String,
)

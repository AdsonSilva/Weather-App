package com.example.weatherapp.view

import com.example.weatherapp.R

enum class WeatherAnimations(val codes: Set<Int>, val animationDay: Int, val animationNight: Int) {
    SUNNY(setOf(113), R.raw.sunny_day, R.raw.sunny_night),
    CLOUDY(setOf(116, 119), R.raw.cloudy_day, R.raw.cloudy_night),
    FOG(setOf(143, 248, 260), R.raw.fog_day, R.raw.fog_night),
    OVERCAST(setOf(112), R.raw.overcast, R.raw.overcast),
    RAINY(setOf(176, 263, 266, 293, 296, 299, 302), R.raw.rain_day, R.raw.rain_night),
    SNOW(setOf(179, 182, 227, 230), R.raw.snow_day, R.raw.snow_night),
    FREEZING_RAIN(setOf(185, 284, 281, 311), R.raw.snow_and_rain_day, R.raw.snow_and_rain_night),
    STRONG_RAIN(setOf(305, 308), R.raw.strong_rain,  R.raw.strong_rain),
    THUNDER(setOf(200), R.raw.thunder,  R.raw.thunder);

    companion object {
        fun getAnimationFromCode(code: Int?): WeatherAnimations{
            return try {
                WeatherAnimations.entries.first{it.codes.contains(code)}
            } catch (e: Exception){
                THUNDER
            }
        }
    }
}
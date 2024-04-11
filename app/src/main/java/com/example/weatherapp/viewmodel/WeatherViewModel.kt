package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.dtos.WeatherDTO
import com.example.weatherapp.view.WeatherAnimations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import retrofit2.HttpException


class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    val weather = MutableLiveData<WeatherDTO>()

    var isRefreshing by mutableStateOf(false)

    fun getWeather() {
        viewModelScope.launch {
            isRefreshing = true
            withContext(Dispatchers.IO) {
                try {
                    val result = weatherRepository.getWeather()
                    weather.postValue(result)
                    isRefreshing = false
                    Log.i("WEATHER", "getWeather: $result")
                } catch (throwable: Throwable) {
                    when(throwable) {
                        is IOException -> {
                            isRefreshing = false
                        }
                        is HttpException -> {
                            isRefreshing = false
                        }
                        else -> {
                            isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    fun getLottieAnimations(): Pair<Int, Int> {
        val weatherCode = weather.value?.current?.weatherCode
        val hour = weather.value?.location?.localtime?.hours ?: 0

        val weatherAnimation = WeatherAnimations.getAnimationFromCode(weatherCode)

        val isNight = (hour > 17 || hour < 4)
        return if (isNight){
            Pair(weatherAnimation.animationNight, R.raw.night_background)
        } else {
            Pair(weatherAnimation.animationDay, R.raw.day_background)
        }
    }
}
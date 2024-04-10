package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.dtos.WeatherDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import retrofit2.HttpException


class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    val weather = MutableLiveData<WeatherDTO>()
    fun getWeather() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = weatherRepository.getWeather()
                    weather.postValue(result)
                    Log.i("WEATHER", "getWeather: $result")
                } catch (throwable: Throwable) {
                    when(throwable) {
                        is IOException -> {

                        }
                        is HttpException -> {

                        }
                        else -> {

                        }
                    }
                }
            }
        }
    }
}
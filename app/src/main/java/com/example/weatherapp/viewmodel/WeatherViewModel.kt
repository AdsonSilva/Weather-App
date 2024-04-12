package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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

    private val _weather = MutableLiveData<WeatherDTO>()
    val weather: LiveData<WeatherDTO> = _weather

    private val _error = MutableLiveData<Boolean>(false)
    val error: LiveData<Boolean> = _error


    fun getWeather(latitude: Double, longitude: Double) {
        val query = "$latitude,$longitude"
        getWeather(query)
    }
    fun getWeather(query: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val result = weatherRepository.getWeather(query)
                    if(result.location != null) {
                        _weather.postValue(result)
                        _error.postValue(false)
                        Log.i("WEATHER", "getWeather: $result")
                    } else {
                        _error.postValue(true)
                    }
                } catch (throwable: Throwable) {
                    _error.postValue(true)
                }
            }
        }
    }

    fun getLottieAnimations(): Pair<Int, Int> {
        val weatherCode = _weather.value?.current?.weatherCode
        val hour = _weather.value?.location?.localtime?.hours ?: 0

        val weatherAnimation = WeatherAnimations.getAnimationFromCode(weatherCode)

        val isNight = (hour > 17 || hour < 4)
        return if (isNight){
            Pair(weatherAnimation.animationNight, R.raw.night_background)
        } else {
            Pair(weatherAnimation.animationDay, R.raw.day_background)
        }
    }

}
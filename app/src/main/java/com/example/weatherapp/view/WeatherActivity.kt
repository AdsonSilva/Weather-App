package com.example.weatherapp.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale


class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObservers()
        refreshWeather()
        swipeRefreshSetup()
    }

    private fun setUpObservers() {
        viewModel.weather.observe(this) { updateScreen() }
        //todo: error
    }

    private fun swipeRefreshSetup() = with(binding.swipeRefresh) {
        setOnRefreshListener {
            refreshWeather()
        }
    }

    private fun refreshWeather() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.getWeather()
    }

    private fun updateScreen() {

        val dateText = SimpleDateFormat("EEEE HH:mm", Locale.ENGLISH).format(viewModel.weather.value?.location?.localtime?.time)
        val temperatureTex = viewModel.weather.value?.current?.temperature.toString() + "º C"
        val humidityText = viewModel.weather.value?.current?.humidity.toString() + "%"
        val windSpeedText = viewModel.weather.value?.current?.windSpeed.toString() + " km/h"
        val feelsLikeText = viewModel.weather.value?.current?.feelslike.toString() + "º C"

        val animations = viewModel.getLottieAnimations()

        with(binding) {
            cityName.text = viewModel.weather.value?.location?.name ?: "-"
            dayAndHour.text = dateText
            animation.setAnimation(animations.first)
            animation.playAnimation()
            backgroundAnimation.setAnimation(animations.second)
            backgroundAnimation.playAnimation()
            temperature.text = temperatureTex
            description.text = viewModel.weather.value?.current?.weatherDescriptions?.get(0) ?: "-"
            humidity.text = humidityText
            windSpeed.text = windSpeedText
            feels.text = feelsLikeText

            swipeRefresh.isRefreshing = false
        }
    }
}
package com.example.weatherapp.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager.*
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityWeatherBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class WeatherActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModel()

    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityWeatherBinding

    private val temperatureMeasure = "º C"
    private val windMeasure = " km/h"
    private val percentage = "%"
    private val requestPermissionCode = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObservers()
        refreshWeather()
        swipeRefreshSetup()

        binding.search.setOnClickListener { searchByCity() }

        binding.inputCity.setOnEditorActionListener { _, actionId, _ ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchByCity()
                handled = true
            }
            handled
        }
    }

    private fun setUpObservers() {
        viewModel.error.observe(this) { dealWithError(it) }
        viewModel.weather.observe(this) { updateScreen() }
    }

    private fun searchByCity() {
        val cityNameToSearch = binding.inputCity.text.toString()

        if(cityNameToSearch.isNotEmpty()) {
            binding.swipeRefresh.isRefreshing = true
            viewModel.getWeather(cityNameToSearch)
        }
        closeKeyboard()
    }

    private fun swipeRefreshSetup() = with(binding.swipeRefresh) {
        setOnRefreshListener {
            refreshWeather()
        }
    }

    private fun refreshWeather() {
        binding.swipeRefresh.isRefreshing = true
        checkLocationPermission()

        val cancellationTokenSource = CancellationTokenSource()

        mFusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token).addOnSuccessListener {
            if(it == null) {
                viewModel.setError()
            } else {
                viewModel.getWeather(it.latitude, it.longitude)
            }
        }
    }

    private fun updateScreen() {

        val dateText = SimpleDateFormat("EEEE HH:mm", Locale.ENGLISH).format(viewModel.weather.value?.location?.localtime?.time)
        val temperatureTex = viewModel.weather.value?.current?.temperature.toString() + temperatureMeasure
        val humidityText = viewModel.weather.value?.current?.humidity.toString() + percentage
        val windSpeedText = viewModel.weather.value?.current?.windSpeed.toString() + windMeasure
        val feelsLikeText = viewModel.weather.value?.current?.feelslike.toString() + temperatureMeasure

        val animations = viewModel.getLottieAnimations()

        with(binding) {
            mainLayout.visibility = VISIBLE
            errorLayout.visibility = INVISIBLE

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

            inputCity.text.clear()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun checkLocationPermission() {
        if(
            ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED
        ) {
            binding.swipeRefresh.isRefreshing = false
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                requestPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == requestPermissionCode && grantResults.isNotEmpty() &&
            grantResults[0] == PERMISSION_GRANTED) {

            refreshWeather()
        }
    }

    private fun closeKeyboard() {
        val view: View = currentFocus as View
        if (currentFocus != null) {
            val manager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            manager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun dealWithError(error: Boolean) {
        if(error) {
            with(binding) {
                mainLayout.visibility = INVISIBLE
                errorLayout.visibility = VISIBLE
                backgroundAnimation.setAnimation(R.raw.error_background)
                backgroundAnimation.playAnimation()
                swipeRefresh.isRefreshing = false
            }
        }
    }
}
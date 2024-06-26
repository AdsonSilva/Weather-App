package com.example.weatherapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApplication)
            androidLogger(Level.DEBUG)
            modules(listOf(
                repositoryModule,
                viewModelModule,
                serviceModule
            ))
        }
    }
}
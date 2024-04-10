package com.example.weatherapp

import com.example.weatherapp.model.WeatherRepository
import com.example.weatherapp.model.WeatherService
import com.example.weatherapp.viewmodel.WeatherViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val viewModelModule = module {
    viewModel {
        WeatherViewModel(get())
    }
}

val repositoryModule = module {
    single {
        WeatherRepository(get())
    }
}

val serviceModule = module {
    single {

        val client: OkHttpClient.Builder =
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )

        Retrofit.Builder()
            .baseUrl("http://api.weatherstack.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build().create(WeatherService::class.java)
    }
}
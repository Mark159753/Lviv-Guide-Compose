package com.example.core.network.weather

import com.example.core.network.model.weather.WeatherResponse

interface WeatherDataSource {

    suspend fun getCurrentWeather(): WeatherResponse?
}
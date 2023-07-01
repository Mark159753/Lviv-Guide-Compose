package com.example.data.repository.weather

import com.example.data.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getWeather(): Flow<WeatherModel>

    suspend fun refresh():Boolean
}
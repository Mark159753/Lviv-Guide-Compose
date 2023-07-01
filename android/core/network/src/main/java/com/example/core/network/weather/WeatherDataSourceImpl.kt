package com.example.core.network.weather

import android.util.Log
import com.example.core.common.di.IoDispatcher
import com.example.core.network.BuildConfig
import com.example.core.network.model.weather.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
):WeatherDataSource {

    override suspend fun getCurrentWeather() = withContext(dispatcher){
        fetchWeather()
    }

    private fun fetchWeather(): WeatherResponse? {
        val apiKey = BuildConfig.WEATHER_API_KEY
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=49.843524&lon=24.026063&appid=$apiKey&units=metric"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        Log.i("WeatherDataSource", url)

        return try {
            val res = client.newCall(request).execute()
            val gson = Gson()
            gson.fromJson(res.body?.string(), WeatherResponse::class.java)
        }catch (e:Exception){
            Log.e("ERROR", e.stackTraceToString())
            null
        }
    }
}
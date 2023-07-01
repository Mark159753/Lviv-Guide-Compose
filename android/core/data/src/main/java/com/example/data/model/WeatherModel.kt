package com.example.data.model

import com.example.core.network.model.weather.WeatherResponse
import com.example.database.entities.WeatherEntity

data class WeatherModel(
    val id:Int,
    val description:String,
    val icon:String,
    val temp:Double
)

fun WeatherResponse.toEntity():WeatherEntity?{
    val weather = weather.firstOrNull() ?: return null
    return WeatherEntity(
        id = weather.id,
        description = weather.description,
        icon = weather.icon,
        temp = main.temp
    )
}

fun WeatherEntity.toExternal(): WeatherModel = WeatherModel(
    id = id,
    description = description,
    icon = "https://openweathermap.org/img/wn/$icon@4x.png",
    temp = temp
)
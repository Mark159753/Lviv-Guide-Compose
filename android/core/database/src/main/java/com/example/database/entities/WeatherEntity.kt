package com.example.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_entity")
data class WeatherEntity(
    @PrimaryKey
    val id:Int,
    val description:String,
    val icon:String,
    val temp:Double
)

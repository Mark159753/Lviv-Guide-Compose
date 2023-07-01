package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.LocalNewsDao
import com.example.database.dao.WeatherDao
import com.example.database.entities.LocalNewsEntity
import com.example.database.entities.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        LocalNewsEntity::class
   ],
    version = 1,
    exportSchema = false
)
abstract class LocalDb: RoomDatabase() {

    abstract fun getWeatherDao():WeatherDao
    abstract fun getLocalNewsDao(): LocalNewsDao
}
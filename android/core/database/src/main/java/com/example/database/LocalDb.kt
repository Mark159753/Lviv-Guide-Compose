package com.example.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.dao.CategoryDao
import com.example.database.dao.LocalNewsDao
import com.example.database.dao.PlacesDao
import com.example.database.dao.UpdateTimeDao
import com.example.database.dao.WeatherDao
import com.example.database.entities.CategoryEntity
import com.example.database.entities.LocalNewsEntity
import com.example.database.entities.PlaceEntity
import com.example.database.entities.UpdateTimeEntity
import com.example.database.entities.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
        LocalNewsEntity::class,
        CategoryEntity::class,
        PlaceEntity::class,
        UpdateTimeEntity::class,
   ],
    version = 1,
    exportSchema = false
)
abstract class LocalDb: RoomDatabase() {

    abstract fun getWeatherDao():WeatherDao
    abstract fun getLocalNewsDao(): LocalNewsDao
    abstract fun getCategoriesDao(): CategoryDao
    abstract fun getPlacesDao():PlacesDao
    abstract fun getUpdateTimeDao(): UpdateTimeDao
}
package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WeatherEntity)

    @Query("SELECT * FROM weather_entity")
    suspend fun getAllItems():List<WeatherEntity>

    @Query("SELECT * FROM weather_entity WHERE id = :id")
    suspend fun getItemById(id:Int): WeatherEntity?

    @Query("SELECT * FROM weather_entity LIMIT 1")
    fun getLastItem():Flow<WeatherEntity>

    @Query("DELETE FROM weather_entity")
    suspend fun deleteAllItems()
}
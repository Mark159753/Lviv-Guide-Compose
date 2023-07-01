package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.LocalNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(item: List<LocalNewsEntity>)

    @Query("SELECT * FROM local_news")
    suspend fun getAllItems():List<LocalNewsEntity>

    @Query("SELECT * FROM local_news WHERE id = :id")
    suspend fun getItemById(id:Int): LocalNewsEntity?

    @Query("SELECT * FROM local_news")
    fun getAllFlow(): Flow<List<LocalNewsEntity>>

    @Query("DELETE FROM local_news")
    suspend fun deleteAllItems()
}
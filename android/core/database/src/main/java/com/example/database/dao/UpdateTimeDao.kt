package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.UpdateTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UpdateTimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: UpdateTimeEntity)

    @Query("SELECT * FROM update_times")
    suspend fun getAllItems():List<UpdateTimeEntity>

    @Query("SELECT * FROM update_times WHERE `key` = :key")
    suspend fun getItemById(key:String): UpdateTimeEntity?

    @Query("SELECT * FROM update_times")
    fun getAllFlow(): Flow<List<UpdateTimeEntity>>

    @Query("DELETE FROM update_times")
    suspend fun deleteAllItems()
}
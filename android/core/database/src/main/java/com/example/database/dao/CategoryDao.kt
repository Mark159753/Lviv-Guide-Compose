package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(item: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CategoryEntity)

    @Query("SELECT * FROM categories")
    suspend fun getAllItems():List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getItemById(id:Int): CategoryEntity?

    @Query("SELECT * FROM categories")
    fun getAllFlow(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM categories")
    suspend fun deleteAllItems()
}
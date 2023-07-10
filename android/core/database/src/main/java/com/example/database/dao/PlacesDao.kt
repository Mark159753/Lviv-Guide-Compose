package com.example.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.database.entities.PlaceEntity
import com.example.database.entities.relation.PlaceWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(item: List<PlaceEntity>)

    @Query("SELECT * FROM places")
    suspend fun getAllItems():List<PlaceEntity>

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getItemById(id:Int): PlaceEntity?

    @Transaction
    @Query("SELECT * FROM places WHERE categoryId = :id OR :id IS NULL")
    fun getAllWithCategories(id:Int?):Flow<List<PlaceWithCategory>>

    @Query("SELECT * FROM places")
    fun getAllFlow(): Flow<List<PlaceEntity>>

    @Query("DELETE FROM places")
    suspend fun deleteAllItems()
}
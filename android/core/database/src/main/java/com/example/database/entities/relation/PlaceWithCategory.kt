package com.example.database.entities.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.database.entities.CategoryEntity
import com.example.database.entities.PlaceEntity


data class PlaceWithCategory(
    @Embedded
    val place:PlaceEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryEntity: CategoryEntity
)

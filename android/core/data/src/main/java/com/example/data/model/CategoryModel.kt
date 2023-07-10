package com.example.data.model

import com.example.FetchCategoriesQuery
import com.example.FetchPlacesWithFiltersQuery
import com.example.database.entities.CategoryEntity

data class CategoryModel(
    val id:Int,
    val name:String
)

fun FetchCategoriesQuery.Category.toEntity() = CategoryEntity(
    id = id,
    name = name
)

fun CategoryEntity.toExternal() = CategoryModel(
    id = id,
    name = name
)

fun FetchPlacesWithFiltersQuery.Place.toCategoryEntity() = CategoryEntity(
    id = mainPlaceInfo.category.id,
    name = mainPlaceInfo.category.name
)

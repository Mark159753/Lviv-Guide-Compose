package com.example.data.model

import com.example.FetchPlacesWithFiltersQuery
import com.example.database.entities.PlaceEntity
import com.example.database.entities.relation.PlaceWithCategory

data class PlaceModel(
    val id:Int,
    val title:String,
    val headImage:String,
    val lat:Double,
    val lon:Double,
    val rating:Float,
    val categoryId:Int,
    val description:String,
    val categoryName:String? = null
)

fun FetchPlacesWithFiltersQuery.Place.toEntity() = PlaceEntity(
    id = mainPlaceInfo.id,
    title = mainPlaceInfo.title,
    headImage = mainPlaceInfo.headImage,
    lat = mainPlaceInfo.location.lat,
    lon = mainPlaceInfo.location.lon,
    categoryId = mainPlaceInfo.category.id,
    rating = mainPlaceInfo.rating.toFloat(),
    description = mainPlaceInfo.description
)

fun PlaceEntity.toExternal() = PlaceModel(
    id = id,
    title = title,
    headImage = headImage,
    lat = lat,
    lon = lon,
    rating = rating,
    categoryId = categoryId,
    description = description
)

fun PlaceWithCategory.toExternal() = PlaceModel(
    id = place.id,
    title = place.title,
    headImage = place.headImage,
    lat = place.lat,
    lon = place.lon,
    rating = place.rating,
    categoryId = place.categoryId,
    categoryName = categoryEntity.name,
    description = place.description
)
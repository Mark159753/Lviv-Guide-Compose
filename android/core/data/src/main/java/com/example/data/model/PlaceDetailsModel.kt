package com.example.data.model

import com.example.FetchPlaceDetailsQuery
import com.example.core.network.BuildConfig
import com.example.fragment.selections.mainPlaceInfoSelections

data class PlaceDetailsModel(
    val id:Int,
    val title:String,
    val headImage:String,
    val lat:Double,
    val lon:Double,
    val rating:Float,
    val categoryName:String,
    val images:List<String>,
    val description:String,
    val address:String
)

fun FetchPlaceDetailsQuery.Place.toExternal() = PlaceDetailsModel(
    id = mainPlaceInfo.id,
    title = mainPlaceInfo.title,
    headImage = BuildConfig.BASE_URL + mainPlaceInfo.headImage,
    lat = mainPlaceInfo.location.lat,
    lon = mainPlaceInfo.location.lon,
    rating = mainPlaceInfo.rating.toFloat(),
    categoryName = mainPlaceInfo.category.name,
    images = images.map { BuildConfig.BASE_URL +  it.path },
    description = description,
    address = address
)

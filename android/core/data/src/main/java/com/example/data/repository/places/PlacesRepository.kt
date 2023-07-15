package com.example.data.repository.places

import com.example.FetchPlaceDetailsQuery
import com.example.core.common.model.refresh.RefreshResult
import com.example.core.common.model.response.ResultWrapper
import com.example.data.model.PlaceDetailsModel
import com.example.data.model.PlaceModel
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {

    val places: Flow<List<PlaceModel>>

    fun getPlacesByCategory(categoryId:Int?): Flow<List<PlaceModel>>

    suspend fun refreshPlaces(force:Boolean = false): RefreshResult

    suspend fun fetchPlaceDetails(id:Int):ResultWrapper<PlaceDetailsModel>
}
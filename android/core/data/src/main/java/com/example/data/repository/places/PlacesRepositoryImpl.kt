package com.example.data.repository.places

import androidx.room.withTransaction
import com.apollographql.apollo3.ApolloClient
import com.example.FetchPlaceDetailsQuery
import com.example.FetchPlacesWithFiltersQuery
import com.example.core.common.di.IoDispatcher
import com.example.core.common.model.refresh.RefreshResult
import com.example.core.common.model.response.ResultWrapper
import com.example.core.common.model.response.toRefreshState
import com.example.data.model.PlaceModel
import com.example.data.model.toCategoryEntity
import com.example.data.model.toEntity
import com.example.data.model.toExternal
import com.example.data.until.CanUpdateHelper
import com.example.data.until.safeExecute
import com.example.database.LocalDb
import com.example.database.dao.CategoryDao
import com.example.database.dao.PlacesDao
import com.example.database.entities.PlaceEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val client: ApolloClient,
    private val localDb: LocalDb,
    private val categoryDao: CategoryDao,
    private val placesDao: PlacesDao,
    private val canUpdateHelper: CanUpdateHelper,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
):PlacesRepository {

    override val places:Flow<List<PlaceModel>>
        get() = placesDao.getAllFlow().map { list -> list.map { it.toExternal() } }

    override fun getPlacesByCategory(categoryId:Int?): Flow<List<PlaceModel>> {
        return placesDao.getAllWithCategories(categoryId).map { list -> list.map { it.toExternal() } }
    }

    override suspend fun refreshPlaces(force:Boolean) = withContext(dispatcher){
        if (force || canUpdateHelper.canUpdate(PlaceEntity::class.java)){
            when(val res = client.query(FetchPlacesWithFiltersQuery()).safeExecute()){
                is ResultWrapper.Error -> res.toRefreshState()
                is ResultWrapper.Success -> {
                    localDb.withTransaction {
                        categoryDao.insertItems(
                            res.value.places.map { it.toCategoryEntity() }
                        )
                        placesDao.deleteAllItems()
                        placesDao.insertItems(
                            res.value.places.map { it.toEntity() }
                        )
                    }
                    canUpdateHelper.save(PlaceEntity::class.java)
                    RefreshResult.Success
                }
            }
        }else{
            RefreshResult.Success
        }
    }

    override suspend fun search(q:String) = withContext(dispatcher){
            placesDao.search(q).map { it.toExternal() }
        }

    override suspend fun fetchPlaceDetails(id:Int) = withContext(dispatcher){
        when(val res = client.query(FetchPlaceDetailsQuery(id)).safeExecute()){
            is ResultWrapper.Error -> res
            is ResultWrapper.Success -> {
                ResultWrapper.Success(res.value.place.toExternal())
            }
        }
    }

}
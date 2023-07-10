package com.example.data.repository.categories

import androidx.room.withTransaction
import com.apollographql.apollo3.ApolloClient
import com.example.FetchCategoriesQuery
import com.example.core.common.di.IoDispatcher
import com.example.core.common.model.refresh.RefreshResult
import com.example.core.common.model.response.ResultWrapper
import com.example.core.common.model.response.toRefreshState
import com.example.data.model.CategoryModel
import com.example.data.model.toEntity
import com.example.data.model.toExternal
import com.example.data.until.CanUpdateHelper
import com.example.data.until.safeExecute
import com.example.database.LocalDb
import com.example.database.dao.CategoryDao
import com.example.database.entities.CategoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoriesRepositoryImpl @Inject constructor(
    private val client:ApolloClient,
    private val localDb: LocalDb,
    private val categoryDao: CategoryDao,
    private val canUpdateHelper: CanUpdateHelper,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
):CategoriesRepository {

    override val categories:Flow<List<CategoryModel>>
        get() = categoryDao.getAllFlow().map { list -> list.map { it.toExternal() } }


    override suspend fun refreshCategories(force:Boolean): RefreshResult {
        return withContext(dispatcher){
            if (force || canUpdateHelper.canUpdate(CategoryEntity::class.java)){
                when(val res = client.query(FetchCategoriesQuery()).safeExecute()){
                    is ResultWrapper.Error -> res.toRefreshState()
                    is ResultWrapper.Success -> {
                        localDb.withTransaction {
                            categoryDao.deleteAllItems()
                            categoryDao.insertItems(res.value.categories.map { it.toEntity() })
                        }
                        canUpdateHelper.save(CategoryEntity::class.java)
                        RefreshResult.Success
                    }
                }
            }else{
                RefreshResult.Success
            }
        }
    }
}
package com.example.data.repository.local_news

import androidx.room.withTransaction
import com.example.core.common.di.IoDispatcher
import com.example.core.common.model.refresh.RefreshResult
import com.example.core.common.model.response.ResultWrapper
import com.example.core.common.model.response.toRefreshState
import com.example.core.network.local_news.LocalNewsSource
import com.example.data.model.LocalNewsModel
import com.example.data.model.toEntity
import com.example.data.model.toExternal
import com.example.data.until.CanUpdateHelper
import com.example.database.LocalDb
import com.example.database.dao.LocalNewsDao
import com.example.database.entities.LocalNewsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalNewsRepositoryImpl @Inject constructor(
    private val localNewsSource: LocalNewsSource,
    private val localNewsDao: LocalNewsDao,
    private val localDb: LocalDb,
    private val canUpdateHelper: CanUpdateHelper,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): LocalNewsRepository {

    override val localNews:Flow<List<LocalNewsModel>>
        get() = localNewsDao.getAllFlow().map { list -> list.map { it.toExternal() } }

    override suspend fun refreshLocalNews(force:Boolean) = withContext(dispatcher){
        if (force || canUpdateHelper.canUpdate(LocalNewsEntity::class.java)){
            when(val res = localNewsSource.getLocalNews()){
                is ResultWrapper.Error -> res.toRefreshState()
                is ResultWrapper.Success -> {
                    val items = res.value.items.map { it.toEntity() }
                    localDb.withTransaction {
                        localNewsDao.deleteAllItems()
                        localNewsDao.insertItems(items)
                    }
                    canUpdateHelper.save(LocalNewsEntity::class.java)
                    RefreshResult.Success
                }
            }
        }else{
            RefreshResult.Success
        }
    }
}
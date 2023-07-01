package com.example.data.repository.local_news

import com.example.core.common.model.response.ResultWrapper
import com.example.core.network.model.local_news.LocalNewsResponse
import com.example.data.model.LocalNewsModel
import kotlinx.coroutines.flow.Flow

interface LocalNewsRepository {

    fun getLocalNews(): Flow<List<LocalNewsModel>>

    suspend fun refreshLocalNews(): ResultWrapper<LocalNewsResponse>
}
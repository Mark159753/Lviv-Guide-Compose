package com.example.data.repository.local_news

import com.example.core.common.model.refresh.RefreshResult
import com.example.core.common.model.response.ResultWrapper
import com.example.core.network.model.local_news.LocalNewsResponse
import com.example.data.model.LocalNewsModel
import kotlinx.coroutines.flow.Flow

interface LocalNewsRepository {

    val localNews:Flow<List<LocalNewsModel>>

    suspend fun refreshLocalNews(force:Boolean = false): RefreshResult
}
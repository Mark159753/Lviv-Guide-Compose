package com.example.core.network.local_news

import com.example.core.common.model.response.ResultWrapper
import com.example.core.network.model.local_news.LocalNewsResponse

interface LocalNewsSource {

    suspend fun getLocalNews(): ResultWrapper<LocalNewsResponse>
}
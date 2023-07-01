package com.example.core.network.local_news

import android.util.Log
import com.example.core.common.EMPTY_RESULT
import com.example.core.common.UNKNOWN_ERROR
import com.example.core.common.di.IoDispatcher
import com.example.core.common.model.response.ResultWrapper
import com.example.core.network.model.local_news.LocalNewsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class LocalNewsSourceImpl @Inject constructor(
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): LocalNewsSource {

    private val newsParser = LocalNewsParser()

    override suspend fun getLocalNews():ResultWrapper<LocalNewsResponse> = withContext(dispatcher){
        fetchLocalNews()
    }

    private fun fetchLocalNews():ResultWrapper<LocalNewsResponse>{
        val url = "https://lviv.tsn.ua/rss/full.rss"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        Log.i("LocalNewsSourceImpl", url)

        return try {
            val res = client.newCall(request).execute()
            if (res.isSuccessful){
                val body = res.body
                    ?: return ResultWrapper.Error(
                        code = res.code,
                        error = null,
                        throwableMessage = EMPTY_RESULT
                    )
                ResultWrapper.Success(newsParser.invoke(body.string().trimIndent()))
            }else{
                ResultWrapper.Error(
                    code = res.code,
                    error = null,
                    throwableMessage = UNKNOWN_ERROR
                )
            }
        }catch (e:Exception){
            Log.e("ERROR", e.stackTraceToString())
            ResultWrapper.Error(
                code = null,
                error = e,
                throwableMessage = e.message
            )
        }
    }
}
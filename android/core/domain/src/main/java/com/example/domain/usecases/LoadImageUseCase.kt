package com.example.domain.usecases

import android.content.Context
import coil.ImageLoader
import coil.imageLoader
import coil.request.ImageRequest
import com.example.core.common.di.IoDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadImageUseCase @Inject constructor(
    @ApplicationContext
    private val context:Context,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(
        url:String,
        size:Int? = null,
        loader: ImageLoader = context.imageLoader
    ) = withContext(dispatcher){
        val request = ImageRequest.Builder(context).apply {
            data(url)
            if (size != null){
                size(size, size)
            }
        }.build()
        loader.enqueue(request).job.await().drawable
    }
}
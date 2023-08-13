package com.example.map.di

import android.content.Context
import com.example.domain.usecases.LoadImageUseCase
import com.example.map.ui.ar.helpers.GenerateViewRenders
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ArHelperModule {
    @Provides
    fun provideGenerateViewNodes(
        imageLoader: LoadImageUseCase,
        @ApplicationContext
        context: Context
    ): GenerateViewRenders{
        return GenerateViewRenders(imageLoader, context)
    }
}
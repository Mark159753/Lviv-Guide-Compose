package com.example.domain.di

import android.content.Context
import com.example.core.common.di.IoDispatcher
import com.example.core.common.di.MainDispatcher
import com.example.domain.usecases.CreatePlacesMarkerDataUseCase
import com.example.domain.usecases.LoadImageUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoadImageUseCase(
        @ApplicationContext
        context:Context,
        @IoDispatcher
        dispatcher: CoroutineDispatcher
    ):LoadImageUseCase{
        return LoadImageUseCase(context, dispatcher)
    }

    @Provides
    fun provideCreatePlacesMarkerDataUseCase(
        @ApplicationContext
        context: Context,
        @MainDispatcher
        dispatcher: CoroutineDispatcher,
        imageLoader: LoadImageUseCase
    ):CreatePlacesMarkerDataUseCase{
        return CreatePlacesMarkerDataUseCase(
            context, dispatcher, imageLoader
        )
    }
}
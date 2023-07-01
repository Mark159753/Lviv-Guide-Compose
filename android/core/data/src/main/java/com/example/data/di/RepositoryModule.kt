package com.example.data.di

import com.example.data.repository.local_news.LocalNewsRepository
import com.example.data.repository.local_news.LocalNewsRepositoryImpl
import com.example.data.repository.weather.WeatherRepository
import com.example.data.repository.weather.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindWeatherRepository(
        repositoryImpl: WeatherRepositoryImpl
    ):WeatherRepository

    @Binds
    abstract fun bindLocalNewsRepository(
        repositoryImpl: LocalNewsRepositoryImpl
    ): LocalNewsRepository
}
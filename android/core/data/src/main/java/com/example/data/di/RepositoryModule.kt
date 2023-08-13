package com.example.data.di

import com.example.data.repository.categories.CategoriesRepository
import com.example.data.repository.categories.CategoriesRepositoryImpl
import com.example.data.repository.local_news.LocalNewsRepository
import com.example.data.repository.local_news.LocalNewsRepositoryImpl
import com.example.data.repository.places.PlacesRepository
import com.example.data.repository.places.PlacesRepositoryImpl
import com.example.data.repository.weather.WeatherRepository
import com.example.data.repository.weather.WeatherRepositoryImpl
import com.example.data.until.CanUpdateHelper
import com.example.data.until.CanUpdateHelperImpl
import com.example.data.until.location.LocationProvider
import com.example.data.until.location.LocationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCanUpdateHelper(
        helperImpl: CanUpdateHelperImpl
    ): CanUpdateHelper

    @Singleton
    @Binds
    abstract fun bindLocationProvider(
        helperImpl: LocationProviderImpl
    ): LocationProvider

    @Binds
    abstract fun bindWeatherRepository(
        repositoryImpl: WeatherRepositoryImpl
    ):WeatherRepository

    @Binds
    abstract fun bindLocalNewsRepository(
        repositoryImpl: LocalNewsRepositoryImpl
    ): LocalNewsRepository

    @Binds
    abstract fun bindCategoriesRepository(
        repositoryImpl: CategoriesRepositoryImpl
    ): CategoriesRepository

    @Binds
    abstract fun bindPlacesRepository(
        repositoryImpl: PlacesRepositoryImpl
    ): PlacesRepository
}
package com.example.core.network.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.core.common.di.IoDispatcher
import com.example.core.network.ApiService
import com.example.core.network.BuildConfig
import com.example.core.network.adapter.NetworkResponseAdapterFactory
import com.example.core.network.local_news.LocalNewsSourceImpl
import com.example.core.network.local_news.LocalNewsSource
import com.example.core.network.weather.WeatherDataSource
import com.example.core.network.weather.WeatherDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) logger.level = HttpLoggingInterceptor.Level.BODY
        else logger.level = HttpLoggingInterceptor.Level.BASIC
        return logger
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(
        client: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideApolloClient(client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BuildConfig.BASE_GRAPTHQL_URL)
            .okHttpClient(client)
            .build()
    }

    @Provides
    fun provideWeatherDataSource(
        @IoDispatcher
        dispatcher: CoroutineDispatcher
    ): WeatherDataSource {
        return WeatherDataSourceImpl(dispatcher)
    }

    @Provides
    fun provideLocalNewsSource(
        @IoDispatcher
        dispatcher: CoroutineDispatcher
    ): LocalNewsSource {
        return LocalNewsSourceImpl(dispatcher)
    }

}
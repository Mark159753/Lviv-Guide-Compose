package com.example.database.di

import android.content.Context
import androidx.room.Room
import com.example.database.LocalDb
import com.example.database.dao.CategoryDao
import com.example.database.dao.LocalNewsDao
import com.example.database.dao.PlacesDao
import com.example.database.dao.UpdateTimeDao
import com.example.database.dao.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalDb {
        return Room.databaseBuilder(context, LocalDb::class.java, "local-db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideWeatherDao(db: LocalDb): WeatherDao = db.getWeatherDao()

    @Provides
    fun provideLocalNewsDao(db: LocalDb): LocalNewsDao = db.getLocalNewsDao()

    @Provides
    fun provideCategoryDao(db: LocalDb): CategoryDao = db.getCategoriesDao()

    @Provides
    fun providePlacesDao(db: LocalDb): PlacesDao = db.getPlacesDao()

    @Provides
    fun provideUpdateTimeDao(db: LocalDb): UpdateTimeDao = db.getUpdateTimeDao()

}
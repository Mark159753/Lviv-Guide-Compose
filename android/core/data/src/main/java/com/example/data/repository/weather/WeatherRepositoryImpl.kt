package com.example.data.repository.weather

import androidx.room.withTransaction
import com.example.core.common.di.IoDispatcher
import com.example.core.network.weather.WeatherDataSource
import com.example.data.model.WeatherModel
import com.example.data.model.toEntity
import com.example.data.model.toExternal
import com.example.database.LocalDb
import com.example.database.dao.WeatherDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource,
    private val weatherDao: WeatherDao,
    private val localDb: LocalDb,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
):WeatherRepository {

    override fun getWeather():Flow<WeatherModel>{
        return weatherDao.getLastItem().map { it.toExternal() }
    }

    override suspend fun refresh():Boolean{
        var isSuccess = false
        return withContext(dispatcher){
            weatherDataSource.getCurrentWeather()?.let { weather ->
                weather.toEntity()?.let { entity ->
                    localDb.withTransaction {
                        weatherDao.deleteAllItems()
                        weatherDao.insertItem(entity)
                    }
                    isSuccess = true
                }
            }
            isSuccess
        }
    }


}
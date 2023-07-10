package com.example.data.until

import com.example.core.common.di.IoDispatcher
import com.example.database.dao.UpdateTimeDao
import com.example.database.entities.UpdateTimeEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class CanUpdateHelperImpl @Inject constructor (
    private val updateDao:UpdateTimeDao,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher
): CanUpdateHelper {

    private val dateNow:Date
        get() = Date(System.currentTimeMillis())

    override suspend fun canUpdate(key:String):Boolean{
        return withContext(dispatcher){
            updateDao.getItemById(key)?.let { lastUpdate ->
                val timeDifference = dateNow.time - lastUpdate.time

                timeDifference > WAITE_TIME
            } ?: true
        }
    }

    override suspend fun <T>canUpdate(key:Class<T>): Boolean {
        return canUpdate(key.name)
    }

    override suspend fun save(key: String){
        withContext(dispatcher){
            updateDao.insertItem(
                UpdateTimeEntity(
                    key = key,
                    time = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun <T> save(key: Class<T>) {
        save(key.name)
    }

    companion object{
        private const val WAITE_TIME = 60 * 60 * 1000 // 1 hour
    }
}
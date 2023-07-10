package com.example.data.until

interface CanUpdateHelper {

    suspend fun canUpdate(key:String):Boolean

    suspend fun <T>canUpdate(key:Class<T>): Boolean

    suspend fun save(key: String)

    suspend fun <T>save(key:Class<T>)
}
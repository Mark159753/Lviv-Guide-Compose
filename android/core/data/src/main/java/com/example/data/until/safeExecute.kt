package com.example.data.until

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation
import com.example.core.common.EMPTY_RESULT
import com.example.core.common.UNKNOWN_ERROR
import com.example.core.common.model.response.ResultWrapper
import kotlinx.coroutines.CancellationException

suspend fun <T: Operation.Data>ApolloCall<T>.safeExecute():ResultWrapper<T>{
    return try {
        val res = execute()
        when{
            res.hasErrors() -> {
                val errorMsg = res.errors?.joinToString { "\n" } ?: UNKNOWN_ERROR
                ResultWrapper.Error(throwableMessage = errorMsg)
            }
            res.data == null ->  ResultWrapper.Error(throwableMessage = EMPTY_RESULT)
            else -> ResultWrapper.Success(value = res.data!!)
        }
    }catch (e:CancellationException) {
        throw e
    }catch (e:Exception){
        ResultWrapper.Error(error = e, throwableMessage = e.message)
    }
}
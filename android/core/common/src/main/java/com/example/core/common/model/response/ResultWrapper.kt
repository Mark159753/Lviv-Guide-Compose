package com.example.core.common.model.response

sealed interface ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>
    data class Error(val code: Int? = null, val error: Throwable? = null, val throwableMessage: String?) : ResultWrapper<Nothing>
}

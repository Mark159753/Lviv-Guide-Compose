package com.example.core.common.model.response

import com.example.core.common.UNKNOWN_ERROR
import com.example.core.common.model.refresh.RefreshResult

sealed interface ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>
    data class Error(val code: Int? = null, val error: Throwable? = null, val throwableMessage: String?) : ResultWrapper<Nothing>
}

fun ResultWrapper.Error.toRefreshState() = RefreshResult.Error(
    msg = throwableMessage ?: UNKNOWN_ERROR,
    throwable = error
)

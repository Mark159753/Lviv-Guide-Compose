package com.example.core.common.model.refresh

sealed interface RefreshResult{
    object Success:RefreshResult
    data class Error(val msg:String?, val throwable: Throwable?):RefreshResult
}
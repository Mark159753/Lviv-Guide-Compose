package com.example.workers.initializers

import kotlinx.coroutines.flow.Flow

interface WorkerInitializer {

    val status:Flow<WorkerStatus>

    fun runWorker()
}

sealed interface WorkerStatus{
    object None:WorkerStatus
    object Running: WorkerStatus
    object Success: WorkerStatus
    data class Error(val msg:String? = null):WorkerStatus
}
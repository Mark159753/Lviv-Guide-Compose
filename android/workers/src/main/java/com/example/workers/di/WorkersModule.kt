package com.example.workers.di

import com.example.workers.initializers.SyncWorkersInitializer
import com.example.workers.initializers.WorkerInitializer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WorkersModule {

    @Binds
    abstract fun bindSyncWorkersInitializer(
        initializer: SyncWorkersInitializer
    ): WorkerInitializer
}
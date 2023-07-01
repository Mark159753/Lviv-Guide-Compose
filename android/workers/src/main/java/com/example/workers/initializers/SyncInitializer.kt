package com.example.workers.initializers

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.workers.SyncNewsWorker
import com.example.workers.SyncWeatherWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SyncWorkersInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
):WorkerInitializer{
    override val status: Flow<WorkerStatus> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SyncWorkersName)
            .map { workInfo ->
                if (workInfo.isEmpty()) return@map WorkerStatus.None
                when{
                    workInfo.any { it.state == WorkInfo.State.RUNNING } -> WorkerStatus.Running
                    workInfo.any { it.state == WorkInfo.State.FAILED } -> WorkerStatus.Error()
                    workInfo.all { it.state == WorkInfo.State.SUCCEEDED } -> WorkerStatus.Success
                    else -> WorkerStatus.None
                }
            }
            .conflate()


    override fun runWorker() {
        WorkManager.getInstance(context).apply {
            enqueueUniqueWork(
                SyncWorkersName,
                ExistingWorkPolicy.KEEP,
                listOf(
                    SyncWeatherWorker.createWorker(),
                    SyncNewsWorker.createWorker()
                ),
            )
        }
    }
}

const val SyncWorkersName = "com.example.workers.initializers.SyncWorkersName"
package com.example.workers

import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.example.core.common.model.response.ResultWrapper
import com.example.data.repository.local_news.LocalNewsRepository
import com.example.workers.helper.syncWorkersConstraints
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncNewsWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val localNewsRepository: LocalNewsRepository
): CoroutineWorker(appContext, workerParams){

    override suspend fun doWork(): Result {
        return when(val res = localNewsRepository.refreshLocalNews()){
            is ResultWrapper.Error -> Result.failure()
            is ResultWrapper.Success -> Result.success()
        }
    }

    companion object{
        fun createWorker() = OneTimeWorkRequestBuilder<SyncNewsWorker>().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                /*
                * On Android versions below 12, setExpedited requires the implementation of
                * getForegroundInfo and displaying a notification; otherwise, it will crash.
                * */
                setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            }
            setConstraints(syncWorkersConstraints)
        }.build()
    }
}
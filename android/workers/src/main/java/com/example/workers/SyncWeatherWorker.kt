package com.example.workers

import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.example.data.repository.weather.WeatherRepository
import com.example.workers.helper.syncWorkersConstraints
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWeatherWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository
): CoroutineWorker(appContext, workerParams){

    override suspend fun doWork(): Result {
        return if (weatherRepository.refresh())
            Result.success()
        else
            Result.failure()
    }

    companion object{
        fun createWorker() = OneTimeWorkRequestBuilder<SyncWeatherWorker>().apply {
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
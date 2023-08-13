package com.example.data.until.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {

    val locationFlow: Flow<Location>

    suspend fun getCurrentLocation(): Location?
}
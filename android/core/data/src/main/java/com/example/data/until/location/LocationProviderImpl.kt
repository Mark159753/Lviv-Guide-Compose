package com.example.data.until.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.example.core.common.exstantion.hasLocationPermission
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationProviderImpl @Inject constructor (
    @ApplicationContext
    private val context: Context
): LocationProvider {

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest: LocationRequest

    private val currentLocationRequest:CurrentLocationRequest by lazy {
        CurrentLocationRequest
            .Builder()
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    }

    override val locationFlow:Flow<Location>
        get() = subscribeOnLocation()

    init {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_UPDATE_INTERVAL).apply {
            setIntervalMillis(LOCATION_UPDATE_INTERVAL)
            setMinUpdateDistanceMeters(MIN_DISTANCE)
            setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            setWaitForAccurateLocation(true)
        }.build()

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? = suspendCoroutine { continuation ->
        if (context.hasLocationPermission()){
            fusedLocationClient.getCurrentLocation(
                currentLocationRequest,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                }
            ).addOnSuccessListener { location -> continuation.resume(location) }
                .addOnCanceledListener { continuation.resume(null) }
                .addOnFailureListener { continuation.resume(null) }
        }else{
            continuation.resume(null)
        }
    }

    @SuppressLint("MissingPermission")
    private fun subscribeOnLocation() = callbackFlow<Location>{
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Log.d(TAG, "New location: ${result.lastLocation.toString()}")

                trySend(result.lastLocation!!)
            }
        }
        if (context.hasLocationPermission()){
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                callback,
                Looper.getMainLooper()
            ).addOnFailureListener { e ->
                close(e)
            }
        }else{
            close(NotHaveLocationPermissionException())
        }
        awaitClose {
            Log.d(TAG, "Stopping location updates")
            fusedLocationClient.removeLocationUpdates(callback)
        }
    }

    companion object{
        private const val TAG = "LocationProvider"

        private const val LOCATION_UPDATE_INTERVAL = 1000L
        private const val MIN_DISTANCE = 5f
    }
}

class NotHaveLocationPermissionException(message:String? = null):Exception(message)
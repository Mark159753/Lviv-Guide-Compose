package com.example.map.ui.ar.helpers

import android.content.Context
import android.widget.Toast
import com.example.core.common.GpsUntil
import com.example.map.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.ar.core.ArCoreApk
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object DeviceCompatibility{

    fun requestGpsIfNotWork(context: Context):Boolean{
        val isEnabled = GpsUntil.isGpsEnabled(context)
        if (!isEnabled){
            GpsUntil.requestEnableGps(context)
        }
        return isEnabled
    }

    @OptIn(ExperimentalPermissionsApi::class)
    fun requestArPermissions(
        locationPermissions: MultiplePermissionsState,
        cameraPermissions: PermissionState,
        context:Context
    ): Boolean {
        when{
            cameraPermissions.status.shouldShowRationale ||
                    locationPermissions.shouldShowRationale -> Toast.makeText(context, R.string.need_permission_rationale, Toast.LENGTH_LONG).show()
            !cameraPermissions.status.isGranted ||
                    !locationPermissions.allPermissionsGranted -> {
                locationPermissions.launchMultiplePermissionRequest()
                cameraPermissions.launchPermissionRequest()
            }
        }
        return locationPermissions.allPermissionsGranted && cameraPermissions.status.isGranted
    }
}
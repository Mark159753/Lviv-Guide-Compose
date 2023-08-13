package com.example.ui.permissions

import android.Manifest
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberCameraPermissions(): PermissionState {
    return rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )
}
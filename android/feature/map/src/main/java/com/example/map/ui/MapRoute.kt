@file:OptIn(ExperimentalMaterialApi::class)

package com.example.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.PlaceMarkerModel
import com.example.ui.components.MapSwitchButton
import com.example.ui.components.SwitchButtonPosition
import com.example.ui.components.rememberMapSwitchState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MapRoute(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel:MapViewModel = hiltViewModel()
){
    MapScreen(
        contentPadding = contentPadding,
        markersData = viewModel.markersData
    )
}


@Composable
private fun MapScreen(
    contentPadding: PaddingValues = PaddingValues(),
    markersData:StateFlow<List<PlaceMarkerModel>> = MutableStateFlow(emptyList())
){

    val switchButtonState = rememberMapSwitchState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){

        AnimatedContent(
            targetState = switchButtonState.swipeState.currentValue,
        ) { state ->
            when(state){
                SwitchButtonPosition.Map -> {
                    MapContent(
                        contentPadding = contentPadding,
                        markersData = markersData
                    )
                }
                SwitchButtonPosition.Ar -> {
                    ArContent()
                }
            }
        }

        MapSwitchButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopEnd),
            state = switchButtonState
        )
    }
}

@Composable
private fun MapContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    markersData:StateFlow<List<PlaceMarkerModel>> = MutableStateFlow(emptyList())
){

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LvivCoordinates, 13f)
    }

    val markersDataState by markersData.collectAsStateWithLifecycle()


    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            contentPadding = contentPadding,
        ){

            markersDataState.forEach { data ->
                Marker(
                    icon = BitmapDescriptorFactory.fromBitmap(data.markerIcon),
                    state = rememberMarkerState(position = LatLng(data.place.lat, data.place.lon))
                )
            }
        }
    }
}

@Composable
fun ArContent(
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxSize()
    )
}

private val LvivCoordinates = LatLng(49.843296, 24.026427)

@Preview
@Composable
private fun MapRoutePreview(){
    Surface {
        MapScreen()
    }
}
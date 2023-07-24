@file:OptIn(ExperimentalMaterialApi::class)

package com.example.map.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.PlaceModel
import com.example.domain.model.PlaceMarkerModel
import com.example.domain.usecases.PlaceMarkerWidth
import com.example.map.R
import com.example.ui.components.MapSwitchButton
import com.example.ui.components.SwitchButtonPosition
import com.example.ui.components.rememberMapSwitchState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt


@Composable
fun MapRoute(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel:MapViewModel = hiltViewModel()
){
    MapScreen(
        contentPadding = contentPadding,
        markersData = viewModel.markersData,
        onSelectPlace = viewModel::selectPlace,
        selectedPlace = viewModel.selectPlace,
        myLocation = viewModel.myLocation
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    contentPadding: PaddingValues = PaddingValues(),
    markersData:StateFlow<List<PlaceMarkerModel>> = MutableStateFlow(emptyList()),
    onSelectPlace:(PlaceModel?)->Unit = {},
    selectedPlace:State<PlaceModel?> = mutableStateOf(null),
    myLocation:StateFlow<LatLng?> = MutableStateFlow(null)
){

    val switchButtonState = rememberMapSwitchState()

    val detailsPlaceBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(key1 = selectedPlace.value, block = {
        if (selectedPlace.value == null)
            detailsPlaceBottomSheetState.hide()
        else
            detailsPlaceBottomSheetState.show()
    })

    selectedPlace.value?.let { data ->
        DetailsBottomSheet(
            data = data,
            sheetState = detailsPlaceBottomSheetState,
            onDismiss = {
                Log.i("DISMISS", data.toString())
                onSelectPlace(null)
            }
        )
    }

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
                        markersData = markersData,
                        onSelectPlace = onSelectPlace,
                        myLocation = myLocation
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
    markersData:StateFlow<List<PlaceMarkerModel>> = MutableStateFlow(emptyList()),
    onSelectPlace:(PlaceModel?)->Unit = {},
    myLocation:StateFlow<LatLng?> = MutableStateFlow(null)
){

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LvivCoordinates, 13f)
    }

    val markersDataState by markersData.collectAsStateWithLifecycle()

    val myLocationState by myLocation.collectAsStateWithLifecycle()

    val isMyLocationAvailable by remember{
        derivedStateOf { myLocationState != null }
    }

    val myLocationMarkerState = rememberMarkerState()

    LaunchedEffect(key1 = myLocationState, block = {
        myLocationState?.let { pos -> myLocationMarkerState.position = pos }
    })


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
                    state = rememberMarkerState(position = LatLng(data.place.lat, data.place.lon)),
                    tag = data.place,
                    onClick = { marker ->
                        onSelectPlace(marker.tag as PlaceModel)
                        true
                    }
                )
            }

            if (isMyLocationAvailable){
                Marker(
                    icon = getBitmapDescriptor(R.drawable.my_location_icon),
                    state = myLocationMarkerState
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

@Composable
private fun getBitmapDescriptor(
    @DrawableRes
    id: Int
): BitmapDescriptor {
    val context = LocalContext.current

    val vectorDrawable = ContextCompat.getDrawable(context, id)!!

    val size = with(LocalDensity.current){ PlaceMarkerWidth.toPx().roundToInt() }
    vectorDrawable.setBounds(0, 0, size, size)
    val bm = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}

private val LvivCoordinates = LatLng(49.843296, 24.026427)

@Preview
@Composable
private fun MapRoutePreview(){
    Surface {
        MapScreen()
    }
}
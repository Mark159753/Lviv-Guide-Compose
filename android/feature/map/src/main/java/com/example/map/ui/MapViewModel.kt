package com.example.map.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.UNKNOWN_ERROR
import com.example.data.model.PlaceModel
import com.example.data.repository.places.PlacesRepository
import com.example.data.until.LocationProvider
import com.example.data.until.location.NotHaveLocationPermissionException
import com.example.domain.usecases.CreatePlacesMarkerDataUseCase
import com.example.ui.theme.bgColors
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    placesRepository:PlacesRepository,
    createMarkerData: CreatePlacesMarkerDataUseCase,
    locationProvider: LocationProvider
):ViewModel() {

    private val _selectedPlace = mutableStateOf<PlaceModel?>(null)
    val selectPlace:State<PlaceModel?>
        get() = _selectedPlace

    val myLocation = locationProvider
        .locationFlow
        .catch { e ->
            if (e is NotHaveLocationPermissionException)
                Log.e("NO_HAVE_PERMISSION", e.message ?: "Location permission not allowed")
            else
                Log.e("ERROR", e.message ?: UNKNOWN_ERROR)
        }
        .map { location ->
            LatLng(location.latitude, location.longitude)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val markersData = placesRepository
        .places
        .map { items ->
            createMarkerData(
                items,
                bgColors
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun selectPlace(place:PlaceModel?){
        _selectedPlace.value = place
    }
}
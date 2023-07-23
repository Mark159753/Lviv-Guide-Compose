package com.example.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.places.PlacesRepository
import com.example.domain.usecases.CreatePlacesMarkerDataUseCase
import com.example.ui.theme.bgColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    placesRepository:PlacesRepository,
    createMarkerData: CreatePlacesMarkerDataUseCase
):ViewModel() {

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
}
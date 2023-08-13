package com.example.placedetails.ui

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.UNKNOWN_ERROR
import com.example.core.common.exstantion.distanceToFormatted
import com.example.core.common.model.response.ResultWrapper
import com.example.data.model.PlaceDetailsModel
import com.example.data.repository.places.PlacesRepository
import com.example.data.until.location.LocationProvider
import com.example.placedetails.navigation.ColorArg
import com.example.placedetails.navigation.IdArg
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext
    context:Context,
    private val placesRepository: PlacesRepository,
    private val locationProvider: LocationProvider,
):ViewModel() {

    private val _state = MutableStateFlow<PlaceDetailsState>(PlaceDetailsState.Loading)
    val state:StateFlow<PlaceDetailsState>
        get() = _state

    val distance = state
        .filter { it is PlaceDetailsState.Success }
        .map {
            val place = (it as PlaceDetailsState.Success).data
            locationProvider.getCurrentLocation()?.distanceToFormatted(
                lat = place.lat,
                lon = place.lon,
                context = context
            )
        }
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "~"
        )

    val colorArg:Int = checkNotNull(savedStateHandle[ColorArg])
    private val placeId:Int = checkNotNull(savedStateHandle[IdArg])

    init {
        getPlaceDetails(placeId)
    }

    private fun getPlaceDetails(id:Int){
        viewModelScope.launch {
            _state.value =  when(val res = placesRepository.fetchPlaceDetails(id)){
                is ResultWrapper.Error -> PlaceDetailsState.Error(
                        msg = res.throwableMessage ?: res.error?.message ?: UNKNOWN_ERROR
                    )
                is ResultWrapper.Success -> PlaceDetailsState.Success(data = res.value)
            }
        }
    }
}

sealed interface PlaceDetailsState{
    object Loading:PlaceDetailsState
    data class Error(val msg:String):PlaceDetailsState
    data class Success(val data:PlaceDetailsModel):PlaceDetailsState
}
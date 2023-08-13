package com.example.map.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.UNKNOWN_ERROR
import com.example.core.common.exstantion.hasLocationPermission
import com.example.data.model.PlaceModel
import com.example.data.repository.places.PlacesRepository
import com.example.data.until.location.LocationProvider
import com.example.data.until.location.NotHaveLocationPermissionException
import com.example.domain.usecases.CreatePlacesMarkerDataUseCase
import com.example.map.ui.ar.helpers.GenerateViewRenders
import com.example.ui.theme.bgColors
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    placesRepository:PlacesRepository,
    createMarkerData: CreatePlacesMarkerDataUseCase,
    locationProvider: LocationProvider,
    @ApplicationContext
    context: Context,
    generateViewRenders: GenerateViewRenders
):ViewModel() {

    private val _selectedPlace = mutableStateOf<PlaceModel?>(null)
    val selectedPlace:State<PlaceModel?>
        get() = _selectedPlace

    private val hasLocationPermission = MutableStateFlow(context.hasLocationPermission())

    val myLocation = hasLocationPermission
        .filter { it }
        .flatMapLatest {
            locationProvider
                .locationFlow
        }
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

    private val places = placesRepository
        .places

    val markersData = places
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

    val dummy = flow<List<PlaceModel>>{
        emit(
            listOf(
                PlaceModel(
                    id = 0,
                    title = "My Home",
                    headImage = "https://i.pinimg.com/550x/6b/ce/97/6bce9718e7c2b14d531034a17d9380c9.jpg",
                    lat = 49.54307571068597,
                    lon = 23.24328620047253,
                    rating = 5f,
                    categoryId = 1,
                    description = "Best Home ever",
                    categoryName = "Home"
                ),
                PlaceModel(
                    id = 0,
                    title = "Home",
                    headImage = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2022/03/the_sun_in_high_resolution/24010613-1-eng-GB/The_Sun_in_high_resolution_pillars.jpg",
                    49.542985, 23.243428,
                    rating = 1f,
                    categoryId = 1,
                    description = "Best Home ever",
                    categoryName = "Home"
                ),

                PlaceModel(
                    id = 1,
                    title = "Home 2",
                    headImage = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2022/03/the_sun_in_high_resolution/24010613-1-eng-GB/The_Sun_in_high_resolution_pillars.jpg",
                    lat = 49.543133,
                    lon = 23.243702,
                    rating = 1f,
                    categoryId = 1,
                    description = "Best Home ever",
                    categoryName = "Home"
                ),PlaceModel(
                    id = 2,
                    title = "Home 3",
                    headImage = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2022/03/the_sun_in_high_resolution/24010613-1-eng-GB/The_Sun_in_high_resolution_pillars.jpg",
                    49.542976, 23.243147,
                    rating = 3f,
                    categoryId = 1,
                    description = "Best Home ever",
                    categoryName = "Home"
                ),PlaceModel(
                    id = 3,
                    title = "Home 4",
                    headImage = "https://www.esa.int/var/esa/storage/images/esa_multimedia/images/2022/03/the_sun_in_high_resolution/24010613-1-eng-GB/The_Sun_in_high_resolution_pillars.jpg",
                    49.542933, 23.243309,
                    rating = 3f,
                    categoryId = 1,
                    description = "Best Home ever",
                    categoryName = "Home"
                ),
            )
        )
    }

    val viewRenders = places.map { place ->
        generateViewRenders(
            places = place,
            colors = bgColors
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun selectPlace(place:PlaceModel?){
        _selectedPlace.value = place
    }

    fun onLocationPermissionChanged(allowed:Boolean){
        hasLocationPermission.value = allowed
    }
}
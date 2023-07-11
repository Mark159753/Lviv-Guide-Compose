package com.example.feature.home.ui.state

import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.data.model.CategoryModel
import com.example.data.model.LocalNewsModel
import com.example.data.model.PlaceModel
import com.example.data.model.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class HomeState(
    val header:HeaderState = HeaderState(),
    val placesListState: PlacesListState = PlacesListState()
)

data class HeaderState(
    val weather: StateFlow<WeatherModel?> = MutableStateFlow(null),
    val localNews: StateFlow<List<LocalNewsModel?>> = MutableStateFlow(List(10){ null }),
    val tabs: StateFlow<List<CategoryModel?>> = MutableStateFlow(List(10){ null }),
)

data class PlacesListState(
    val places: StateFlow<List<PlaceModel?>> = MutableStateFlow(List(10){ null }),
    val currentLocation: State<Location?> = mutableStateOf(null)
)

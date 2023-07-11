package com.example.feature.home.ui

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.categories.CategoriesRepository
import com.example.data.repository.local_news.LocalNewsRepository
import com.example.data.repository.places.PlacesRepository
import com.example.data.repository.weather.WeatherRepository
import com.example.data.until.LocationProvider
import com.example.feature.home.ui.state.HeaderState
import com.example.feature.home.ui.state.HomeState
import com.example.feature.home.ui.state.PlacesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    localNewsRepository: LocalNewsRepository,
    placesRepository: PlacesRepository,
    categoriesRepository: CategoriesRepository,
    private val locationProvider: LocationProvider,
):ViewModel() {

    private val selectedCategory = MutableStateFlow<Int?>(null)

    private val weather = weatherRepository
        .getWeather()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val localNews = localNewsRepository
        .localNews
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = List(10){ null }
        )

    private val categories = categoriesRepository
        .categories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = List(5){ null }
        )

    private val currentLocation = mutableStateOf<Location?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val places = selectedCategory
        .flatMapLatest { categoryId ->
            placesRepository.getPlacesByCategory(categoryId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = List(10){ null }
        )


    val uiState = HomeState(
        header = HeaderState(
            weather = weather,
            localNews = localNews,
            tabs = categories
        ),
        placesListState = PlacesListState(
            places = places,
            currentLocation = currentLocation
        )
    )

    fun selectCategory(categoryId:Int?){
        selectedCategory.value = categoryId
    }

    fun requestCurrentLocation(){
        viewModelScope.launch {
            currentLocation.value = locationProvider.getCurrentLocation()
        }
    }
}
package com.example.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.LocalNewsModel
import com.example.data.repository.categories.CategoriesRepository
import com.example.data.repository.local_news.LocalNewsRepository
import com.example.data.repository.places.PlacesRepository
import com.example.data.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    weatherRepository: WeatherRepository,
    localNewsRepository: LocalNewsRepository,
    placesRepository: PlacesRepository,
    categoriesRepository: CategoriesRepository,
):ViewModel() {

    private val selectedCategory = MutableStateFlow<Int?>(null)

    val weather = weatherRepository.getWeather()

    val localNews = localNewsRepository
        .localNews
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = arrayOfNulls<LocalNewsModel>(10).toList()
        )

    val categories = categoriesRepository.categories

    @OptIn(ExperimentalCoroutinesApi::class)
    val places = selectedCategory
        .flatMapLatest { categoryId ->
            placesRepository.getPlacesByCategory(categoryId)
        }

    fun selectCategory(categoryId:Int?){
        selectedCategory.value = categoryId
    }
}
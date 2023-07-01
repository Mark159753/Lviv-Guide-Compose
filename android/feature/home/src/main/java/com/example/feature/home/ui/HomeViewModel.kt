package com.example.feature.home.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.LocalNewsModel
import com.example.data.repository.local_news.LocalNewsRepository
import com.example.data.repository.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val localNewsRepository: LocalNewsRepository
):ViewModel() {

    val weather = weatherRepository.getWeather()

    val localNews = localNewsRepository
        .getLocalNews()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = arrayOfNulls<LocalNewsModel>(10).toList()
        )

}
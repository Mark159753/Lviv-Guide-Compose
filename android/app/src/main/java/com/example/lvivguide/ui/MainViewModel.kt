package com.example.lvivguide.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workers.initializers.WorkerInitializer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workersInitializer: WorkerInitializer
):ViewModel() {

    init {
        fun observeSyncState(){
            viewModelScope.launch {
                workersInitializer.status.collectLatest { state ->
                    Log.i("STATE", state.toString())
                }
            }
        }
    }
}
package ui.places

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.AllPlacesQuery
import data.repository.PlacesRepository
import data.repository.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlacesController(
    private val placesRepository: PlacesRepository
) {

    val state = MutableStateFlow<PlacesState>(PlacesState.Loading)
    private val scope = CoroutineScope(SupervisorJob())

    var errorMsg by mutableStateOf<String?>(null)

    init {
        loadPlaces()
    }

    fun loadPlaces(){
        scope.launch {
            when(val res = placesRepository.fetchAll()){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    println(errorMsg)
                    state.value = PlacesState.Error(msg = errorMsg)
                }
                is Result.Success -> {
                    res.data?.places?.let { data ->
                        state.value = PlacesState.Success(data = data)
                    }
                }
            }
        }
    }

    fun deletePlace(id:Int){
        scope.launch {
            when(val res = placesRepository.removePlace(id)){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    println(errorMsg)
                    state.value = PlacesState.Error(msg = errorMsg)
                }
                is Result.Success -> {
                    loadPlaces()
                }
            }
        }
    }

    fun clearError(){
        errorMsg = null
    }

}

sealed interface PlacesState{
    object Loading: PlacesState
    data class Error(val msg:String): PlacesState
    data class Success(val data:List<AllPlacesQuery.Place>): PlacesState
}
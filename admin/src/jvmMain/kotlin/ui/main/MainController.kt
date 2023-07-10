package ui.main

import com.example.AllCategoriesQuery
import data.repository.CategoriesRepository
import data.repository.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainController(
    private val categoriesRepository: CategoriesRepository
) {

    val state = MutableStateFlow<MainState>(MainState.Loading)

    private val scope = CoroutineScope(SupervisorJob())

    init {
        scope.launch {
            loadCategories()
        }
    }

    private suspend fun loadCategories(){
        when(val res = categoriesRepository.fetchAllCategories()){
            is Result.Error -> {
                val errorMsg = res.errors.joinToString { it.message + "\n" }
                println(errorMsg)
                state.value = MainState.Error(msg = errorMsg)
            }
            is Result.Success -> {
                res.data?.categories?.let { data ->
                    state.value = MainState.Success(data = data)
                }
            }
        }
    }
}

sealed interface MainState{
    object Loading: MainState
    data class Error(val msg:String): MainState
    data class Success(val data:List<AllCategoriesQuery.Category>): MainState
}
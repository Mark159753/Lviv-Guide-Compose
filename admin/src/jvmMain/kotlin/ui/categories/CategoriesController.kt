package ui.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.AllCategoriesQuery
import data.repository.CategoriesRepository
import data.repository.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CategoriesController(
    private val categoriesRepository: CategoriesRepository
) {


    val state = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    private val scope = CoroutineScope(SupervisorJob())

    var errorMsg by mutableStateOf<String?>(null)

    init {
        loadCategories()
    }

    fun loadCategories(){
        scope.launch {
            when(val res = categoriesRepository.fetchAllCategories()){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    println(errorMsg)
                    state.value = CategoriesState.Error(msg = errorMsg)
                }
                is Result.Success -> {
                    res.data?.categories?.let { data ->
                        state.value = CategoriesState.Success(data = data)
                    }
                }
            }
        }
    }

    fun deleteCategory(id:Int){
        scope.launch {
            when(val res = categoriesRepository.removeCategory(id)){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    this@CategoriesController.errorMsg = errorMsg
                    println(errorMsg)
                }
                is Result.Success -> {
                    loadCategories()
                }
            }
        }
    }

    fun createCategory(name:String){
        scope.launch {
            when(val res = categoriesRepository.createCategory(name)){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    this@CategoriesController.errorMsg = errorMsg
                    println(errorMsg)
                }
                is Result.Success -> {
                    loadCategories()
                }
            }
        }
    }

    fun clearError(){
        errorMsg = null
    }

}

sealed interface CategoriesState{
    object Loading: CategoriesState
    data class Error(val msg:String): CategoriesState
    data class Success(val data:List<AllCategoriesQuery.Category>): CategoriesState
}
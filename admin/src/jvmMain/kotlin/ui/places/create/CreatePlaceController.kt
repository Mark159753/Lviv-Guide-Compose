package ui.places.create

import androidx.compose.runtime.*
import com.apollographql.apollo3.api.toUpload
import com.example.type.CreateLocationInput
import com.example.type.CreatePlaceInput
import data.repository.PlacesRepository
import data.repository.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File

class CreatePlaceController(
    private val placesRepository: PlacesRepository
) {

    private val scope = CoroutineScope(SupervisorJob())

    var errorMsg by mutableStateOf<String?>(null)

    var headImage by mutableStateOf<String?>(null)
    var title by mutableStateOf<String>("")
    var description by  mutableStateOf<String>("")
    var lat by mutableStateOf<String>("")
    var lon by mutableStateOf<String>("")
    var rating by mutableStateOf<String>("")
    var address by mutableStateOf<String>("")
    var categoryId by mutableStateOf<String>("")

    val images = mutableStateListOf<String>()


    fun createPlace(){
        scope.launch {
            val input = CreatePlaceInput(
                title = title,
                description = description,
                headImage = File(headImage!!).toUpload("image"),
                rating = rating.toDouble(),
                address = address,
                location = CreateLocationInput(
                    lat = lat.toDouble(),
                    lon = lon.toDouble()
                ),
                categoryId = categoryId.toInt(),
                images = images.toList().map { File(it).toUpload("image") }
            )
            when(val res = placesRepository.createPlace(input)){
                is Result.Error -> {
                    val errorMsg = res.errors.joinToString { it.message + "\n" }
                    this@CreatePlaceController.errorMsg = errorMsg
                    println(errorMsg)
                }
                is Result.Success ->{
                    clearAll()
                }
            }
        }
    }

    fun clearError(){
        errorMsg = null
    }

    private fun clearAll(){
        clearError()
        headImage = null
        title = ""
        description = ""
        lat = ""
        lon = ""
        rating = ""
        address = ""
        categoryId = ""
        images.clear()
    }
}
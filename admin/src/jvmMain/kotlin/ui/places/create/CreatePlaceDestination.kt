package ui.places.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import navigation.NavigationState
import navigation.Screens
import navigation.rememberNavigation
import org.koin.compose.koinInject
import ui.dialogs.error.ErrorDialog
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
fun CreatePlaceDestination(
    navigator: NavigationState = rememberNavigation(Screens.CreatePlace),
    controller: CreatePlaceController = koinInject()
){

    if (!controller.errorMsg.isNullOrBlank()){
        ErrorDialog(
            msg = controller.errorMsg ?: "",
            onDismiss = controller::clearError,
            title = "Error"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        PlacesToolbar(
            onBackClick = { navigator.navBack() },
            onSave = {
                controller.createPlace()
            }
        )

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            HeadImage(
                filePath = controller.headImage,
                onFileChosen = { controller.headImage = it }
            )

            TitleDescription(
                title = controller.title,
                onTitleChanged = { controller.title = it },
                description = controller.description,
                onDescriptionChanged = { controller.description = it }
            )

        }

        LocationRating(
            lat = controller.lat,
            onLatChange = { controller.lat = it },
            lon = controller.lon,
            onLonChanged = { controller.lon = it },
            rating = controller.rating,
            onRatingChanged = { controller.rating = it }
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ParamText(
                title = "Address:",
                value = controller.address,
                onValueChanged = { controller.address = it }
            )

            ParamText(
                title = "Category ID:",
                value = controller.categoryId,
                onValueChanged = { controller.categoryId = it }
            )
        }

        ImagesList(
            images = controller.images,
            onAddClick = {
                val files = openMultipleFileDialog()
                if (files.isNotEmpty()){
                    controller.images.addAll(files.map { it.absolutePath })
                }
            },
            onRemoveImage = {
                controller.images.remove(it)
            }
        )

    }
}

@Composable
private fun HeadImage(
    modifier: Modifier = Modifier,
    filePath:String? = null,
    onFileChosen:(String?)->Unit = {}
){
    Box(
        modifier = modifier
            .border(width = 1.dp, color = Color(0xFFbababa))
            .clickable {
                openFileDialog()?.let {
                    onFileChosen(it)
                }
            }
            .width(265.dp)
            .aspectRatio(1.1f),
        contentAlignment = Alignment.Center
    ){

        Text(
            text = "Click to choose photo",
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Center
        )

        filePath?.let {
            Image(
                bitmap = imageFromFile(File(it)),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

}

@Composable
private fun ImagesList(
    modifier: Modifier = Modifier,
    onAddClick:()->Unit = {},
    images:List<String> = emptyList(),
    onRemoveImage:(String)->Unit = {}
){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Images",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onAddClick
            ){
                Text(text = "Add")
            }
        }

        LazyRow(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(
                count = images.size
            ){
                val dataItem = images[it]
                ImageItem(
                    image = dataItem,
                    onRemoveClick = onRemoveImage
                )
            }
        }
    }
}

@Composable
private fun ImageItem(
    image:String,
    modifier: Modifier = Modifier,
    onRemoveClick:(item:String) -> Unit = {}
){
    Box(
        modifier = modifier
            .height(155.dp)
            .aspectRatio(1f)
    ){
        Image(
            bitmap = imageFromFile(File(image)),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { onRemoveClick(image) }
        ){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun LocationRating(
    modifier: Modifier = Modifier,
    lat:String = "",
    onLatChange:(String)->Unit = {},
    lon:String = "",
    onLonChanged:(String) -> Unit = {},
    rating:String = "",
    onRatingChanged:(String) -> Unit = {}
){
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ParamText(
            title = "Lat:",
            value = lat,
            onValueChanged = onLatChange
        )

        ParamText(
            title = "Lon:",
            value = lon,
            onValueChanged = onLonChanged
        )

        ParamText(
            title = "Rating:",
            value = rating,
            onValueChanged = onRatingChanged
        )
    }
}

@Composable
private fun ParamText(
    title: String,
    value:String,
    onValueChanged:(String)->Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
                .widthIn(120.dp, 200.dp),
            value = value,
            onValueChange = onValueChanged,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
    }
}

@Composable
private fun TitleDescription(
    modifier: Modifier = Modifier,
    title:String = "",
    onTitleChanged: (String) -> Unit = {},
    description:String = "",
    onDescriptionChanged:(String)->Unit,
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Title",
            style = MaterialTheme.typography.h6
        )
        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            value = title,
            onValueChange = onTitleChanged,
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )

        Text(
            text = "Description",
            style = MaterialTheme.typography.h6
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(1f),
            value = description,
            onValueChange = onDescriptionChanged,
            textStyle = MaterialTheme.typography.body1,
            maxLines = 5
        )
    }
}

@Composable
private fun PlacesToolbar(
    modifier: Modifier = Modifier,
    onBackClick:()->Unit = {},
    onSave:()->Unit = {}
){
    Row(
        modifier = modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
        }
        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Text(
            modifier = Modifier.weight(1f),
            text = "Create Place",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        TextButton(
            onClick = onSave,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onPrimary
            )
        ){
            Text(
                text = "Save"
            )
        }
    }
}


private fun openFileDialog():String?{
    val dialog = FileDialog(null as Frame?, "Select File to Open")
    dialog.mode = FileDialog.LOAD
    dialog.isVisible = true
    val file: String? = dialog.file
    val directory = dialog.directory
    return if (file.isNullOrBlank()) null else "$directory$file"
}

private fun openMultipleFileDialog():Array<File>{
    val dialog = FileDialog(null as Frame?, "Select File to Open")
    dialog.mode = FileDialog.LOAD
    dialog.isMultipleMode = true
    dialog.isVisible = true
    return dialog.files
}

fun imageFromFile(file: File): ImageBitmap {
    return org.jetbrains.skia.Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap()
}
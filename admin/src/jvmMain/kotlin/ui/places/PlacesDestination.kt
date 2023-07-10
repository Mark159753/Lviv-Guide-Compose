package ui.places

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.AllPlacesQuery
import navigation.NavigationState
import navigation.Screens
import navigation.rememberNavigation
import org.koin.compose.koinInject
import ui.categories.ErrorMsg
import ui.categories.LoadingState
import ui.dialogs.error.ErrorDialog
import ui.dialogs.question.QuestionDialog

@Composable
fun PlacesDestination(
    navigator: NavigationState = rememberNavigation(Screens.Places),
    controller: PlacesController = koinInject()
){

    val state by controller.state.collectAsState()

    if (!controller.errorMsg.isNullOrBlank()){
        ErrorDialog(
            msg = controller.errorMsg ?: "",
            onDismiss = controller::clearError,
            title = "Error"
        )
    }

    var itemForDelete by remember { mutableStateOf<AllPlacesQuery.Place?>(null) }

    if (itemForDelete != null){
        QuestionDialog(
            msg =  "Do you really want delete this place?",
            onDismiss = { itemForDelete = null },
            title = "Delete",
            onConfirm = {
                itemForDelete?.let { item ->
                    controller.deletePlace(item.id)
                }
                itemForDelete = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        PlacesToolbar(
            onBackClick = { navigator.navBack() },
            onCreate = { navigator.navTo(Screens.CreatePlace) }
        )

        when(state){
            is PlacesState.Error -> {
                ErrorMsg(
                    msg = (state as? PlacesState.Error)?.msg ?: "",
                    onRetry = { controller.loadPlaces() }
                )
            }
            PlacesState.Loading -> LoadingState()
            is PlacesState.Success -> {
                ListContent(
                    list = (state as? PlacesState.Success)?.data ?: emptyList(),
                    onRemove = {
                        itemForDelete = it
                    }
                )
            }
        }
    }
}

@Composable
fun PlacesToolbar(
    modifier: Modifier = Modifier,
    onBackClick:()->Unit = {},
    onCreate:()->Unit = {}
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
            text = "Places",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onPrimary
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        TextButton(
            onClick = onCreate,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colors.onPrimary
            )
        ){
            Text(
                text = "Create"
            )
        }
    }
}

@Composable
private fun ListContent(
    list: List<AllPlacesQuery.Place>,
    onRemove:(item:AllPlacesQuery.Place)->Unit = {}
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ){
        items(
            count = list.size,
            key = { index -> list[index].id }
        ){ index ->
            val dataItem = list[index]

            ListItem(
                item = dataItem,
                onRemove = onRemove
            )

            Divider(
                color = Color.Black,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ListItem(
    item:AllPlacesQuery.Place,
    modifier: Modifier = Modifier,
    onRemove:(item:AllPlacesQuery.Place)->Unit = {}
){
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .widthIn(min = 50.dp)
                .fillMaxWidth(0.1f),
            text = item.id.toString(),
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.weight(1f),
            text = item.title
        )
        Spacer(
            modifier = Modifier.width(12.dp)
        )
        Text(
            modifier = Modifier.weight(0.5f),
            text = item.category.name
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Button(
            onClick = { onRemove(item) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.error,
                contentColor = MaterialTheme.colors.onError
            )
        ){
            Text("Remove")
        }
    }
}

@Preview
@Composable
private fun PlacesDestinationPreview(){
    PlacesDestination()
}
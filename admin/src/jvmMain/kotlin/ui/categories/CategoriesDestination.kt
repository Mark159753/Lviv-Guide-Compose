package ui.categories

import androidx.compose.desktop.ui.tooling.preview.Preview
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
import com.example.AllCategoriesQuery
import navigation.NavigationState
import navigation.Screens
import navigation.rememberNavigation
import org.koin.compose.koinInject
import ui.dialogs.error.ErrorDialog
import ui.dialogs.question.QuestionDialog

@Composable
fun CategoriesDestination(
    navigator: NavigationState = rememberNavigation(Screens.Categories),
    controller: CategoriesController = koinInject()
){
    val state by controller.state.collectAsState()

    if (!controller.errorMsg.isNullOrBlank()){
        ErrorDialog(
            msg = controller.errorMsg ?: "",
            onDismiss = controller::clearError,
            title = "Error"
        )
    }

    var itemForDelete by remember { mutableStateOf<AllCategoriesQuery.Category?>(null) }

    if (itemForDelete != null){
        QuestionDialog(
            msg =  "Do you really want delete this category?",
            onDismiss = { itemForDelete = null },
            title = "Delete",
            onConfirm = {
                itemForDelete?.let { item ->
                    controller.deleteCategory(item.id)
                }
                itemForDelete = null
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()){
        Toolbar(
            onBackClick = { navigator.navBack() },
            onAddClick = {
                controller.createCategory(it)
            }
        )

        when(state){
            is CategoriesState.Error -> {
                ErrorMsg(
                    msg = (state as? CategoriesState.Error)?.msg ?: "",
                    onRetry = { controller.loadCategories() }
                )
            }
            CategoriesState.Loading -> LoadingState()
            is CategoriesState.Success -> {
                ContentList(
                    list = (state as? CategoriesState.Success)?.data ?: emptyList(),
                    onRemove = {
                        itemForDelete = it
                    }
                )
            }
        }
    }
}

@Composable
private fun Toolbar(
    modifier: Modifier = Modifier,
    onBackClick:()->Unit = {},
    onAddClick:(name:String)->Unit = {}
){

    var categoryName by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .height(50.dp)
                .weight(1f),
            value = categoryName,
            onValueChange = {
                categoryName = it
            },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Button(
            onClick = {
                if (categoryName.isNotBlank()){
                    onAddClick(categoryName)
                    categoryName = ""
                }
            }
        ){
            Text(
                text = "Add"
            )
        }
    }
}

@Composable
fun ContentList(
    list: List<AllCategoriesQuery.Category>,
    onRemove:(item:AllCategoriesQuery.Category)->Unit = {}
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
            CategoryItem(
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
fun LoadingState(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator(
            modifier = Modifier.size(150.dp)
        )
    }
}

@Composable
private fun CategoryItem(
    item: AllCategoriesQuery.Category,
    modifier: Modifier = Modifier,
    onRemove:(item:AllCategoriesQuery.Category)->Unit = {}
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
            text = item.name
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

@Composable
fun ErrorMsg(
    msg:String,
    onRetry:()->Unit = {}
){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column {
            Text(text = msg)
            Button(
                onClick = onRetry
            ){
                Text("Retry")
            }
        }
    }
}

@Preview
@Composable
private fun CategoriesDestinationPreview(){
    CategoriesDestination()
}
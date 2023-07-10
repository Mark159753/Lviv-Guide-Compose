package ui.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigation.NavigationState
import navigation.Screens
import navigation.rememberNavigation

@Composable
fun HomeDestination(
    navigator:NavigationState = rememberNavigation(Screens.Home)
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Admin panel",
            style = MaterialTheme.typography.h4
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            HomeItem(
                modifier = Modifier.fillMaxWidth(),
                title = "Places",
                onClick = {
                    navigator.navTo(Screens.Places)
                }
            )

            HomeItem(
                modifier = Modifier.fillMaxWidth(),
                title = "Categories",
                onClick = {
                    navigator.navTo(Screens.Categories)
                }
            )
        }
    }
}

@Composable
fun HomeItem(
    modifier: Modifier = Modifier,
    title:String,
    onClick:()->Unit
){
    Button(
        modifier = modifier,
        onClick = onClick
    ){
        Text(
            text = title
        )
    }
}

@Preview
@Composable
private fun HomeDestinationPreview(){
    HomeDestination()
}
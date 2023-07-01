package com.example.feature.home.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.LocalNewsModel
import com.example.data.model.WeatherModel
import com.example.feature.home.R
import com.example.ui.components.NestedScrollColumn
import com.example.ui.components.ScrollablePlacesTabsRow
import com.example.ui.components.TabItem
import com.example.ui.components.rememberNestedScrollState
import com.example.ui.components.rememberTabsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeRoute(
    contentPadding: PaddingValues,
    viewModel:HomeViewModel = hiltViewModel()
){
    HomeScreen(
        contentPadding = contentPadding,
        weather = viewModel.weather,
        localNews = viewModel.localNews
    )
}

@Composable
private fun HomeScreen(
    contentPadding: PaddingValues = PaddingValues(),
    weather: Flow<WeatherModel> = flowOf(),
    localNews: Flow<List<LocalNewsModel?>> = flowOf()
){

    val nestedScrollState = rememberNestedScrollState()

    val isSticky = nestedScrollState.isMaxOffsetReached

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        NestedScrollColumn(
            state = nestedScrollState,
            header = {
                Header(
                    isSticky = isSticky,
                    weather = weather,
                    localNews = localNews
                )
            },
            lazyColumn = {
                PlacesList(
                    modifier = Modifier,
                    contentPadding = contentPadding
                )
            },
            stickyHeaderHeight = 80.dp
        )
    }

    AnimatedVisibility(
        visible = isSticky,
        enter = fadeIn()
    ) {
        Spacer(modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .statusBarsPadding()
        )
    }

}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    isSticky:Boolean = false,
    weather: Flow<WeatherModel> = flowOf(),
    localNews: Flow<List<LocalNewsModel?>>
){

    val weatherState by weather.collectAsStateWithLifecycle(initialValue = null)
    val localNewsState by localNews.collectAsStateWithLifecycle(initialValue = arrayOfNulls<LocalNewsModel>(10).toList())

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.home_screen_header_title),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            )

            if (weatherState != null){
                WeatherWidget(
                    weather = weatherState!!,
                    modifier = Modifier.weight(1f)
                )
            }
        }


        Text(
            text = stringResource(id = R.string.home_screen_last_news_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ){
            items(
                count = localNewsState.size,
            ){ index ->
                NewsItem(
                    item = localNewsState[index]
                )
            }
        }

        Text(
            text = stringResource(id = R.string.home_screen_places_to_explore_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        SearchStub(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
        )

        TabsPlaces(
            isSticky = isSticky,
            onClick = {}
        )

    }
}

@Composable
private fun SearchStub(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.home_screen_search_hint_title),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TabsPlaces(
    modifier: Modifier = Modifier,
    isSticky: Boolean = false,
    onClick:(index:Int)->Unit
){

    val tabState = rememberTabsState(selectedPosition = 0)

    val elevation by animateDpAsState(targetValue = if (isSticky) 4.dp else 0.dp)

    Surface(
        modifier = modifier
        .shadow(elevation)
    ) {

        ScrollablePlacesTabsRow(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp),
            state = tabState,
            content = {
                Tabs.values().forEachIndexed { index, tab ->
                    TabItem(
                        onClick = {
                            tabState.selectedPosition = index
                            onClick(index)
                        }
                    ) {
                        val isSelected = remember {
                            derivedStateOf { tabState.selectedPosition == index }
                        }
                        val textColor =
                            animateColorAsState(if (isSelected.value) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)

                        Text(
                            text = tab.name,
                            color = textColor.value
                        )
                    }
                }
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.filter_icon),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            indicatorColor = Color.Black
        )
    }
}

enum class Tabs{
    All, Museum, Places, Theaters, Restorants, Parks
}

@Composable
private fun PlacesList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = (contentPadding.calculateBottomPadding() + 16.dp)),
        content = {
            items(
                count = 20
            ){
                PlaceItem()
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}

@Preview
@Composable
fun HomeRoutePreview(){
    Surface {
        HomeScreen()
    }
}
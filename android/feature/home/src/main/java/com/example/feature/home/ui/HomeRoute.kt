package com.example.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
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
import com.example.data.model.CategoryModel
import com.example.feature.home.R
import com.example.feature.home.ui.state.HeaderState
import com.example.feature.home.ui.state.HomeState
import com.example.feature.home.ui.state.PlacesListState
import com.example.ui.components.NestedScrollColumn
import com.example.ui.components.ScrollablePlacesTabsRow
import com.example.ui.components.TabItem
import com.example.ui.components.rememberNestedScrollState
import com.example.ui.components.rememberTabsState
import com.example.ui.permissions.rememberLocationPermissions
import com.example.ui.theme.bgColors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeRoute(
    contentPadding: PaddingValues,
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
    onNavToSearch:()->Unit = {},
    viewModel:HomeViewModel = hiltViewModel()
){

    val locationPermission = rememberLocationPermissions()

    LaunchedEffect(key1 = Unit ){
        if (!locationPermission.allPermissionsGranted)
            locationPermission.launchMultiplePermissionRequest()

        snapshotFlow { locationPermission.allPermissionsGranted }
            .distinctUntilChanged()
            .collectLatest { isGranted ->
                if (isGranted)
                    viewModel.requestCurrentLocation()
            }
    }


    HomeScreen(
        contentPadding = contentPadding,
        homeState = viewModel.uiState,
        onCategoryChange = viewModel::selectCategory,
        onPlaceClick = onPlaceClick,
        onNavToSearch = onNavToSearch
    )
}

@Composable
private fun HomeScreen(
    contentPadding: PaddingValues = PaddingValues(),
    homeState: HomeState = HomeState(),
    onCategoryChange: (categoryId: Int?) -> Unit = {},
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
    onNavToSearch:()->Unit = {},
){

    val nestedScrollState = rememberNestedScrollState()

    val isSticky = nestedScrollState.isMaxOffsetReached

    val placesListState = rememberLazyStaggeredGridState()

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        NestedScrollColumn(
            state = nestedScrollState,
            header = {
                Header(
                    isSticky = isSticky,
                    headerState = homeState.header,
                    onCategoryChange = { id ->
                        onCategoryChange(id)
                        scope.launch {
                            delay(300)
                            placesListState.scrollToItem(0)
                        }
                    },
                    onNavToSearch = onNavToSearch
                )
            },
            scrolableContent = {
                PlacesList(
                    modifier = Modifier,
                    contentPadding = contentPadding,
                    listState = placesListState,
                    placesState = homeState.placesListState,
                    onPlaceClick = onPlaceClick
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
    headerState: HeaderState = HeaderState(),
    onCategoryChange:(categoryId:Int?)->Unit = {},
    onNavToSearch:()->Unit = {}
){

    val weatherState by headerState.weather.collectAsStateWithLifecycle()
    val localNewsState by headerState.localNews.collectAsStateWithLifecycle()

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
                .padding(top = 8.dp),
            onClick = onNavToSearch
        )

        TabsPlaces(
            isSticky = isSticky,
            onClick = onCategoryChange,
            tabs = headerState.tabs
        )

    }
}

@Composable
private fun SearchStub(
    modifier: Modifier = Modifier,
    onClick:()->Unit = {}
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
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
    onClick:(categoryId:Int?)->Unit,
    tabs: StateFlow<List<CategoryModel?>> = MutableStateFlow(List(5){ null })
){

    val tabsAsState by tabs.collectAsStateWithLifecycle()

    val tabState = rememberTabsState(selectedPosition = 0)

    val elevation by animateDpAsState(targetValue = if (isSticky) 4.dp else 0.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation)
    ) {

        ScrollablePlacesTabsRow(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp),
            state = tabState,
            content = {
                
                val isLoading = tabsAsState.any { it == null }
                
                TabItem(
                    modifier = Modifier
                        .placeholder(
                            visible = isLoading,
                            color = bgColors.random(),
                            highlight = PlaceholderHighlight.fade(),
                            shape = RoundedCornerShape(100)
                        ),
                    onClick = {
                        if (isLoading) return@TabItem
                        tabState.selectedPosition = 0
                        onClick(null)
                    }
                ) {
                    val isSelected = remember {
                        derivedStateOf { tabState.selectedPosition == 0 }
                    }
                    val textColor =
                        animateColorAsState(if (isSelected.value) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)

                    Text(
                        text = stringResource(id = R.string.home_screen_tabs_categories_all),
                        color = textColor.value
                    )
                }
                
                tabsAsState.forEachIndexed { index, categoryModel ->
                    val position = index + 1
                    TabItem(
                        modifier = Modifier
                            .then(
                                if (categoryModel == null) Modifier.width(100.dp) else Modifier
                            )
                            .placeholder(
                                visible = categoryModel == null,
                                color = bgColors.random(),
                                highlight = PlaceholderHighlight.fade(),
                                shape = RoundedCornerShape(100)
                            ),
                        onClick = {
                            if (categoryModel == null) return@TabItem
                            tabState.selectedPosition = position
                            onClick(categoryModel.id)
                        }
                    ) {
                        val isSelected = remember {
                            derivedStateOf { tabState.selectedPosition == position }
                        }
                        val textColor =
                            animateColorAsState(if (isSelected.value) Color.White else MaterialTheme.colorScheme.onSurfaceVariant)

                        Text(
                            text = categoryModel?.name ?: "",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlacesList(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    listState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    placesState: PlacesListState = PlacesListState(),
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->}
){

    val placesAsState by placesState.places.collectAsStateWithLifecycle()

    LazyVerticalStaggeredGrid(
        state = listState,
        columns = StaggeredGridCells.Adaptive(160.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = (contentPadding.calculateBottomPadding() + 16.dp)),
        content = {
            items(
                count = placesAsState.size,
                key = if (placesAsState.firstOrNull() == null) null else { index -> placesAsState[index]!!.id }
            ){ index ->
                val dataItem = placesAsState.getOrNull(index)
                PlaceItem(
                    item = dataItem,
                    modifier = Modifier.animateItemPlacement(),
                    currentLocation = placesState.currentLocation,
                    onItemClick = onPlaceClick
                )
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
package com.example.placedetails.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import com.example.data.model.PlaceDetailsModel
import com.example.placedetails.R
import com.example.ui.components.CollapsingToolbar
import com.example.ui.components.ErrorScreen
import com.example.ui.components.LoadingRow
import com.example.ui.components.LoadingTextBlock
import com.example.ui.components.NestedScrollColumn
import com.example.ui.components.RatingBar
import com.example.ui.components.ToolbarHeight
import com.example.ui.components.rememberNestedScrollState
import com.example.ui.theme.bgColors
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PlaceDetailsRoute(
    onNavBack:()->Unit = {},
    viewModel:PlaceDetailsViewModel = hiltViewModel()
){
    PlaceDetailsScreen(
        placeColor = Color(viewModel.colorArg),
        onBackClick = onNavBack,
        stateFlow = viewModel.state,
        distanceFlow = viewModel.distance
    )
}


@Composable
private fun PlaceDetailsScreen(
    placeColor:Color = MaterialTheme.colorScheme.primary,
    onBackClick:()->Unit = {},
    stateFlow:StateFlow<PlaceDetailsState> = MutableStateFlow(PlaceDetailsState.Loading),
    distanceFlow:StateFlow<String> = MutableStateFlow("~"),
){

    val nestedScrollState = rememberNestedScrollState()

    val statusBarHeight = with(LocalDensity.current){ WindowInsets.statusBars.getTop(this).toDp() }

    val state by stateFlow.collectAsStateWithLifecycle()

    if (state is PlaceDetailsState.Error){
        ErrorScreen(
            msg = (state as PlaceDetailsState.Error).msg,
            onBackClick = onBackClick
        )
    }else{
        NestedScrollColumn(
            stickyHeaderHeight = ToolbarHeight + statusBarHeight,
            state = nestedScrollState,
            header = {
                CollapsingToolbar(
                    title = (state as? PlaceDetailsState.Success)?.data?.title,
                    image = (state as? PlaceDetailsState.Success)?.data?.headImage,
                    scrollProvider = { nestedScrollState.scrollOffset.toInt() },
                    progress = { nestedScrollState.process },
                    backgroundColor = placeColor,
                    onBackClick = onBackClick
                )
            },
            scrolableContent = {
                when(state){
                    PlaceDetailsState.Loading -> LoadingContent()
                    is PlaceDetailsState.Success -> LoadedContent(
                        placeData = (state as PlaceDetailsState.Success).data,
                        distance = distanceFlow
                    )
                    else -> {}
                }
            }
        )
    }

}

@Composable
private fun LoadedContent(
    placeData:PlaceDetailsModel,
    distance:StateFlow<String> = MutableStateFlow("~")
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
    ) {

        Spacer(modifier = Modifier.height(12.dp))
        HeadInfo(
            rating = placeData.rating,
            distanceFlow = distance,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Category(
            category = placeData.categoryName,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (placeData.images.isNotEmpty()){
            Title(
                title = stringResource(id = R.string.place_details_gallery_title),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            GalleryList(
                images = placeData.images
            )

            Spacer(modifier = Modifier.height(16.dp))
        }


        Title(
            title = stringResource(id = R.string.place_details_about_title),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = placeData.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        LoadingRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.6f)
                .height(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoadingRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.35f)
                .height(20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LoadingRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.3f)
                .height(30.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        )  {
            items(
                count = 6,
            ){
                ImageItem(
                    source = "",
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LoadingRow(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(0.3f)
                .height(30.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LoadingTextBlock(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            rows = 10,
            rowHeight = 20.dp,
            rowSpace = 8.dp
        )
    }
}

@Composable
private fun HeadInfo(
    modifier: Modifier = Modifier,
    rating:Float,
    distanceFlow:StateFlow<String> = MutableStateFlow("~")
){

    val distance by distanceFlow.collectAsStateWithLifecycle()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RatingBar(
            rating = rating,
            modifier = Modifier.height(12.dp),
            space = 4.dp
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = rating.toString(),
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            painter = painterResource(id = R.drawable.location_on),
            contentDescription = null,
            modifier = Modifier
                .size(16.dp)
                .padding(end = 2.dp),
            tint = Color.Black
        )
        Text(
            text = distance,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Category(
    modifier: Modifier = Modifier,
    category:String
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .background(bgColors.random())
            .wrapContentSize()
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    title:String
){
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
}

@Composable
private fun GalleryList(
    modifier: Modifier = Modifier,
    images:List<String> = emptyList(),
    onItemClick:(path:String, index:Int)->Unit = {_, _ ->}
){
  LazyRow(
      modifier = modifier,
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
  )  {
        items(
            count = images.size,
            key = { index -> images[index] }
        ){ index ->
            val dataItem = images[index]
            ImageItem(
                source = dataItem,
                modifier = Modifier
                    .clickable {
                        onItemClick(dataItem, index)
                    }
            )
        }
  }
}

@Composable
private fun ImageItem(
    modifier: Modifier = Modifier,
    source:String,
    onClick:(path:String)->Unit = {}
){

    val context = LocalContext.current

    var isLoading by remember{ mutableStateOf(true) }

    AsyncImage(
        model = source,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        imageLoader = context.imageLoader,
        modifier = modifier
            .height(110.dp)
            .aspectRatio(1.4f)
//            .clickable {
//                if (source.isNotBlank()){
//                    onClick(source)
//                }
//            }
            .placeholder(
                visible = isLoading,
                color = bgColors.random(),
                highlight = PlaceholderHighlight.fade()
            ),
        onState = {
            isLoading = it !is AsyncImagePainter.State.Success
        }
    )
}

@Preview
@Composable
private fun PlaceDetailsScreenPreview(){
    Surface {
        PlaceDetailsScreen()
    }
}
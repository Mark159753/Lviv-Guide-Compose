package com.example.feature.home.ui

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import com.example.core.common.BuildConfig
import com.example.core.common.exstantion.safeDistanceToFormatted
import com.example.data.model.PlaceModel
import com.example.feature.home.R
import com.example.ui.theme.bgColors
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    item: PlaceModel? = null,
    currentLocation: State<Location?> = mutableStateOf(null)
){

    val bgColor = remember {
        bgColors.random()
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(bottom = 16.dp)
    ){

        val context = LocalContext.current
        var isLoading by remember{ mutableStateOf(true) }

        AsyncImage(
            model = BuildConfig.BASE_URL + item?.headImage,
            imageLoader = context.imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .placeholder(
                    visible = isLoading,
                    color = bgColors.random(),
                    highlight = PlaceholderHighlight.fade()
                )
                .aspectRatio(1f),
            onState = {
                isLoading = it !is AsyncImagePainter.State.Success
            }
        )

        Text(
            text = item?.title ?: "",
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        if (item != null){
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                RatingBlock(rating = item.rating.toString())
                Spacer(modifier = Modifier.width(4.dp))
                LocationBloc(
                    item = item,
                    currentLocation = currentLocation
                )
            }
        }
    }
}

@Composable
private fun RatingBlock(
    rating:String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.star_rate_24),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .padding(end = 2.dp),
            tint = Color.Black
        )
        Text(
            text = rating,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun LocationBloc(
    item: PlaceModel,
    modifier: Modifier = Modifier,
    currentLocation: State<Location?> = mutableStateOf(null)
){
    val context = LocalContext.current
    val distance by remember(currentLocation) {
        mutableStateOf(currentLocation.value.safeDistanceToFormatted(
            lat = item.lat,
            lon = item.lon,
            context = context,
        ))
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
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
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun PlaceItemPreview(){
    PlaceItem()
}
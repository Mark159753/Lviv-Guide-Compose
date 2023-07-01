package com.example.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.example.feature.home.R
import com.example.ui.theme.bgColors

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColors.random())
            .padding(bottom = 16.dp)
    ){

        val context = LocalContext.current

        Image(
            painter = rememberAsyncImagePainter("", context.imageLoader),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Text(
            text = "Metropolitan Gardens",
            color = Color.Black,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            RatingBlock(rating = "4.7")
            Spacer(modifier = Modifier.width(4.dp))
            LocationBloc(location = "2 km")
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
    location:String,
    modifier: Modifier = Modifier
){
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
            text = location,
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
package com.example.feature.home.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import com.example.data.model.LocalNewsModel
import com.example.ui.theme.bgColors
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

@Composable
fun NewsItem(
    item:LocalNewsModel?,
    modifier: Modifier = Modifier,
    onClick:(url:String)-> Unit = {}
){

    val context = LocalContext.current

    var isLoading by remember{ mutableStateOf(true) }

    Column(modifier = modifier
        .width(150.dp)
        .clickable { item?.let { onClick(it.link) } })
    {
        AsyncImage(
            model = item?.image,
            contentDescription = item?.description,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .placeholder(
                    visible = isLoading,
                    color = bgColors.random(),
                    highlight = PlaceholderHighlight.fade()
                )
                .fillMaxWidth()
                .aspectRatio(1.1f),
            contentScale = ContentScale.Crop,
            imageLoader = context.imageLoader,
            onState = {
                isLoading = it !is AsyncImagePainter.State.Success
            }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Text(
            text = item?.title ?: "",
            style = MaterialTheme.typography.labelMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        )
    }
}

@Preview
@Composable
private fun NewsItemPreview(){
    NewsItem(
        item = LocalNewsModel(
            id = 0,
            title = "Something important happened",
            description = "",
            link = "",
            pubDate = "",
            guid = "",
            image = "",
            fulltext = ""
        )
    )
}
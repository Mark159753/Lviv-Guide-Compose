package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import com.example.ui.theme.bgColors
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.placeholder

val ToolbarHeight = 55.dp

@Composable
fun CollapsingToolbar(
    modifier: Modifier = Modifier,
    scrollProvider: () -> Int = { 0 },
    progress: () -> Float = { 0f },
    title: String?,
    image:String?,
    backgroundColor:Color = MaterialTheme.colorScheme.primary,
    onBackClick: () -> Unit
){

    val context = LocalContext.current
    var titleHeight by remember {
        mutableIntStateOf(0)
    }

    var isLoading by remember{ mutableStateOf(true) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            val toolbarProgress = {
                ((progress() - 0.78f) / (1f - 0.78f)).coerceIn(0f, 1f)
            }

            val imageProgress = {
                val min = 0.46f
                val max = 0.94f

                val clampedValue = progress().coerceIn(min, max)
                (clampedValue - min) / (max - min)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                        .graphicsLayer {
                            val p = imageProgress()
                            translationY = titleHeight * p
                        }
                ){
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .clipToBounds()
                            .placeholder(
                                visible = isLoading,
                                color = bgColors.random(),
                                highlight = PlaceholderHighlight.fade()
                            )
                            .graphicsLayer {
                                translationY = (-scrollProvider() / 2f)
                            },
                        imageLoader = context.imageLoader,
                        onState = {
                            isLoading = it !is AsyncImagePainter.State.Success
                        }
                    )

                    val scrimProgress = {
                        val min = 0.4f
                        val max = 0.86f

                        val clampedValue = progress().coerceIn(min, max)
                        (clampedValue - min) / (max - min)
                    }

                    Scrim(
                        fraction = scrimProgress,
                        color = backgroundColor,
                        modifier = Modifier.matchParentSize()
                    )

                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { titleHeight = it.size.height }
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    if (title != null){
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .graphicsLayer {
                                    alpha = 1f - toolbarProgress()
                                }
                        )
                    }else{
                        LoadingRow(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(0.8f)
                                .height(40.dp)
                        )
                    }
                }
            }

            Toolbar(
                title = title ?: "",
                progress = toolbarProgress,
                modifier = Modifier
                    .offset {
                        val scroll = -scrollProvider()
                        IntOffset(x = 0, y = scroll)
                    },
                onBackClick = onBackClick
            )

        }
    }
}

@Composable
private fun Toolbar(
    title:String,
    modifier: Modifier = Modifier,
    progress:()->Float = { 1f },
    onBackClick:()->Unit = {},
){

    val screenHeight = with(LocalConfiguration.current){ screenHeightDp.dp }
    val titleStartPos = with(LocalDensity.current){ screenHeight.toPx() * 0.126f }

    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .height(ToolbarHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null)
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .graphicsLayer {
                    val p = progress()
                    translationY = (titleStartPos * (1f - p))
                    alpha = p
                }
        )
    }
}

@Composable
private fun Scrim(
    fraction: () -> Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
    ) {
        drawRect(color, alpha = fraction())
    }
}

@Preview
@Composable
private fun CollapsingToolbarPreview(){
    CollapsingToolbar(
        scrollProvider = { 0 },
        title = "Hello",
        image = "",
        onBackClick = {}
    )
}
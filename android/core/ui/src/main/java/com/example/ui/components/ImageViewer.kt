package com.example.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SwipeProgress
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ImageViewer(
    onDismiss:()->Unit,
    images:List<String> = emptyList(),
    initPage:Int = 0
){
    
    val pagerState = rememberPagerState(
        initialPage = initPage,
        pageCount = { images.size }
    )

    val systemUiController = rememberSystemUiController()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val swipeState = rememberSwipeableState(
        initialValue = SwipeState.SwipesBottom,
    )

    val screenHeight = with(LocalConfiguration.current){ screenHeightDp.dp }
    val maxOffset = with(LocalDensity.current){ screenHeight.toPx()}

    val anchors = mapOf(0f to SwipeState.Idle, maxOffset to SwipeState.SwipesBottom, -maxOffset to SwipeState.SwipedTop)

    BackHandler {
        onDismiss()
    }

    LaunchedEffect(key1 = Unit, block = {
        swipeState.animateTo(SwipeState.Idle)
        snapshotFlow { swipeState.currentValue }.collectLatest { state ->
            if (state == SwipeState.SwipesBottom || state == SwipeState.SwipedTop){
                onDismiss()
            }
        }
    })

    DisposableEffect(key1 = Unit){
        systemUiController.systemBarsDarkContentEnabled = false
        onDispose {
            systemUiController.systemBarsDarkContentEnabled = true
        }
    }

    Box(
        modifier = Modifier
            .zIndex(100f)
            .drawBehind {
                drawRoundRect(
                    Color.Black,
                    alpha = calcProgress(swipeState.progress)
                )
            }
            .fillMaxSize()
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier,
            beyondBoundsPageCount = 3
        ) { index ->

            val painter = rememberAsyncImagePainter(
                model = images[index],
                imageLoader = context.imageLoader
            )
            val zoomState = rememberZoomState(
                contentSize = painter.intrinsicSize.takeIf { it != Size.Unspecified } ?: Size.Zero,
                maxScale = 4f
            )

            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .offset {
                        IntOffset(x = 0, y = swipeState.offset.value.roundToInt())
                    }
                    .swipeable(
                        state = swipeState,
                        orientation = Orientation.Vertical,
                        anchors = anchors,
                        thresholds = { _, _ -> FractionalThreshold(0.3f) }
                    )
                    .zoomable(
                        zoomState = zoomState,
                        onDoubleTap = { position ->
                            val targetScale = when {
                                zoomState.scale < 2f -> 2f
                                zoomState.scale < 4f -> 4f
                                else -> 1f
                            }
                            zoomState.changeScale(targetScale, position)
                        },
                    ),
            )

            val isVisible = index == pagerState.settledPage
            LaunchedEffect(isVisible) {
                if (!isVisible) {
                    zoomState.reset()
                }
            }
        }

        val closeBtnAlpha = {
            val p = calcProgress(swipeState.progress)
            ((p - 0.7f) / (1f - 0.7f)).coerceIn(0f, 1f)
        }

        IconButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 4.dp, start = 16.dp)
                .graphicsLayer {
                    alpha = closeBtnAlpha()
                },
            onClick = {
                scope.launch {
                    swipeState.animateTo(SwipeState.SwipesBottom)
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun calcProgress(progress: SwipeProgress<SwipeState>): Float {
    return when{
        progress.to == SwipeState.Idle && progress.from == SwipeState.Idle -> 1f
        progress.from == SwipeState.Idle -> 1f - progress.fraction
        (progress.to == SwipeState.SwipedTop && progress.from == SwipeState.SwipedTop)
                || (progress.to == SwipeState.SwipesBottom && progress.from == SwipeState.SwipesBottom) -> 0f
        else -> progress.fraction
    }

}

private enum class SwipeState{
    Idle, SwipedTop, SwipesBottom
}


@Preview
@Composable
private fun ImageViewerDialogPreview(){
    val images = listOf(
        "https://www.humanesociety.org/sites/default/files/2019/02/dog-451643.jpg",
        "https://www.humanesociety.org/sites/default/files/2019/02/dog-451643.jpg",
        "https://www.humanesociety.org/sites/default/files/2019/02/dog-451643.jpg",
    )

    ImageViewer(
        onDismiss = {},
        images = images
    )
}
package com.example.ui.components

import androidx.compose.animation.core.animateRect
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.math.roundToInt

@Stable
class TabsState(
    initSelectedPosition:Int,
){
    var selectedPosition by mutableIntStateOf(initSelectedPosition)

    internal val tabsInfo: SnapshotStateMap<Int, TabInfo> = mutableStateMapOf()

    internal val selectedTabInfo by derivedStateOf { tabsInfo[selectedPosition] }

    companion object{
        fun saver() = Saver<TabsState, Int>(
            save = { it.selectedPosition },
            restore = { TabsState(it) }
        )
    }
}

@Composable
fun rememberTabsState(selectedPosition:Int): TabsState {
    return rememberSaveable(saver = TabsState.saver()) {
        TabsState(selectedPosition)
    }
}

data class TabInfo(
    val width:Int,
    val height:Int,
    val xOffset:Int
)

@Composable
fun ScrollablePlacesTabsRow(
    content: @Composable ()->Unit,
    modifier: Modifier = Modifier,
    state: TabsState = rememberTabsState(0),
    edgePadding: Dp = 16.dp,
    dividerSpace:Dp = 4.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.tertiary,
    leadingIcon: @Composable (() -> Unit)? = null,
){

    val divider = with(LocalDensity.current) { dividerSpace.toPx() }.roundToInt()

    val transition = updateTransition(state.selectedTabInfo, label = "box state")

    val selectedRect = transition.animateRect(label = "rect") { info ->
        if (info == null) Rect(0f,0f,0f,0f)
        else
        Rect(
            offset = Offset(x = info.xOffset.toFloat(), 0f),
            size = Size(
                width = info.width.toFloat(),
                height = info.height.toFloat()
            ),
        )
    }

    val scrollState = rememberScrollState()

    Row(modifier = modifier
        .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {

        leadingIcon?.let { icon ->
            Box(modifier = Modifier.padding(start = edgePadding, end = edgePadding)){
                icon()
            }
        }

        val edgeStartPadding = if (leadingIcon == null) edgePadding else 0.dp

        Box{
            Spacer(
                modifier = Modifier
                    .padding(start = edgeStartPadding, end = edgePadding)
                    .offset { IntOffset(x = selectedRect.value.topLeft.x.roundToInt(), y = 0) }
                    .clip(RoundedCornerShape(100))
                    .background(indicatorColor)
                    .width(with(LocalDensity.current) { selectedRect.value.size.width.toDp() })
                    .height(with(LocalDensity.current) { selectedRect.value.size.height.toDp() })
            )
            Layout(
                content = content,
                modifier = Modifier
                    .padding(start = edgeStartPadding, end = edgePadding)
            ) { measurables, constraints ->
                val placeables = measurables.map { measurable ->
                    measurable.measure(constraints)
                }

                var xPos = 0
                val h = placeables.maxOfOrNull { it.height } ?: 0
                val maxW = placeables.sumOf { (it.width + divider) } - divider
                val w = min(maxW, constraints.maxWidth)

                layout(width = w, height = h) {
                    placeables.forEachIndexed { index, placeable ->
                        placeable.placeRelative(x = xPos, y = 0)
                        state.tabsInfo[index] = TabInfo(
                            width = placeable.width,
                            height = placeable.height,
                            xOffset = (xPos)
                        )
                        xPos += (placeable.width + divider)
                    }
                }
            }

        }
    }
}


@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    onClick: () ->Unit = {},
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100))
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {
        content()
    }
}

@Preview
@Composable
private fun ScrollablePlacesTabsRowPreview(){

    val state = rememberTabsState(selectedPosition = 0)

    Surface() {
        ScrollablePlacesTabsRow(content = {
            repeat(5){

                TabItem(
                    onClick = {
                        state.selectedPosition = it
                    }
                ) {
                    Text(
                        text = "TAB -> $it",
                    )
                }
            }
        },
            state = state
        )
    }
}
package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ui.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SwitchButtonPosition{
    Map,
    Ar
}

@OptIn(ExperimentalMaterialApi::class)
@Stable
class MapSwitchState(
    initialValue: SwitchButtonPosition = SwitchButtonPosition.Map,
    internal val confirmStateChange:(state:SwitchButtonPosition)->Boolean = { true }
){

    var currentState by mutableStateOf<SwitchButtonPosition>(initialValue)
        internal set

    val swipeState = SwipeableState(
        initialValue = initialValue,
        confirmStateChange = { state ->
            val isConfirmed = confirmStateChange(state)
            if (isConfirmed){
                currentState = state
            }
            isConfirmed
        }
    )

    val progress:Float
        get() {
           return when{
               (swipeState.progress.from == SwitchButtonPosition.Ar
                        && swipeState.progress.to == SwitchButtonPosition.Map)
                       || (swipeState.progress.from == SwitchButtonPosition.Map
                       && swipeState.progress.to == SwitchButtonPosition.Map)
                -> 1 - swipeState.progress.fraction
               (swipeState.progress.from == SwitchButtonPosition.Map &&
                        swipeState.progress.to == SwitchButtonPosition.Ar) ||
                       (swipeState.progress.from == SwitchButtonPosition.Ar &&
                               swipeState.progress.to == SwitchButtonPosition.Ar) ->
                    swipeState.progress.fraction
                else -> swipeState.progress.fraction
            }
        }

    companion object{
        internal fun saver() = Saver<MapSwitchState, SwitchButtonPosition>(
            save = { it.swipeState.currentValue },
            restore = { MapSwitchState(it) }
        )
    }
}

@Composable
fun rememberMapSwitchState(
    initialValue: SwitchButtonPosition = SwitchButtonPosition.Map,
    confirmStateChange:(state:SwitchButtonPosition)->Boolean = { true }
): MapSwitchState {
    return rememberSaveable(saver = MapSwitchState.saver()) {
        MapSwitchState(initialValue, confirmStateChange)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapSwitchButton(
    modifier: Modifier = Modifier,
    trackShape: Shape = RoundedCornerShape(8.dp),
    thumbShape: Shape = RoundedCornerShape(8.dp),
    thumbColor:Color = MaterialTheme.colorScheme.primary,
    trackColor:Color = MaterialTheme.colorScheme.surface,
    state: MapSwitchState = rememberMapSwitchState()
){

    val tintColor = MaterialTheme.colorScheme.onSurface

    val thumbSize = 50.dp
    val trackWidth = thumbSize * 2

    val scope = rememberCoroutineScope()

    val sizePx = with(LocalDensity.current) { thumbSize.toPx() }
    val anchors = mapOf(0f to SwitchButtonPosition.Map, sizePx to SwitchButtonPosition.Ar)

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(thumbSize)
            .clip(trackShape)
            .background(trackColor)
            .swipeable(
                state = state.swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ){

        Spacer(
            modifier = Modifier
                .offset { IntOffset(state.swipeState.offset.value.roundToInt(), 0) }
                .size(thumbSize)
                .clip(thumbShape)
                .background(thumbColor)
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {

            IconButton(
                onClick = {
                    if (state.confirmStateChange(SwitchButtonPosition.Map)){
                        scope.launch {
                            state.swipeState.animateTo(SwitchButtonPosition.Map)
                            state.currentState = SwitchButtonPosition.Map
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.location_marker),
                    contentDescription = "map",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(14.dp),
                    tint = lerp(trackColor, tintColor, state.progress)
                )
            }

            IconButton(
                onClick = {
                    if (state.confirmStateChange(SwitchButtonPosition.Ar)){
                        scope.launch {
                            state.swipeState.animateTo(SwitchButtonPosition.Ar)
                            state.currentState = SwitchButtonPosition.Ar
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ar_glasses), 
                    contentDescription = "ar",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(8.dp),
                    tint = lerp(tintColor, trackColor, state.progress)
                )
            }

        }
    }
}

@Preview
@Composable
private fun MapSwitchButtonPreview(){
    MapSwitchButton()
}
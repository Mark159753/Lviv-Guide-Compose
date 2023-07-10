@file:OptIn(ExperimentalMaterialApi::class)

package com.example.map.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui.components.MapSwitchButton
import com.example.ui.components.SwitchButtonPosition
import com.example.ui.components.rememberMapSwitchState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapRoute(
    contentPadding: PaddingValues = PaddingValues()
){
    MapScreen(
        contentPadding = contentPadding
    )
}


@Composable
private fun MapScreen(
    contentPadding: PaddingValues = PaddingValues()
){

    val lviv = LatLng(49.843296, 24.026427)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lviv, 13f)
    }

    val switchButtonState = rememberMapSwitchState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){

        AnimatedContent(
            targetState = switchButtonState.swipeState.currentValue,
        ) { state ->
            when(state){
                SwitchButtonPosition.Map -> {
                    GoogleMap(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = cameraPositionState,
                        contentPadding = contentPadding
                    )
                }
                SwitchButtonPosition.Ar -> {
                    ArContent()
                }
            }
        }

        MapSwitchButton(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .align(Alignment.TopEnd),
            state = switchButtonState
        )
    }
}

@Composable
fun ArContent(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxSize()
    )
}



@Preview
@Composable
private fun MapRoutePreview(){
    Surface {
        MapScreen()
    }
}
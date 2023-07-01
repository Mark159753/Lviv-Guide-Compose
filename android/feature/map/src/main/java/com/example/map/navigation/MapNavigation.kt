package com.example.map.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.map.ui.MapRoute
import com.google.accompanist.navigation.animation.composable

const val MapNavigationRoute = "map_route"

fun NavController.navigateToMap(navOptions: NavOptions? = null) {
    this.navigate(MapNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.mapScreen(
    contentPadding: PaddingValues = PaddingValues()
) {
    composable(
        route = MapNavigationRoute,
    ) {
        MapRoute(
            contentPadding = contentPadding
        )
    }
}
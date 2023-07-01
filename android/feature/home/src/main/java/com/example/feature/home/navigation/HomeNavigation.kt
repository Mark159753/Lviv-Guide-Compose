package com.example.feature.home.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.feature.home.ui.HomeRoute
import com.google.accompanist.navigation.animation.composable

const val HomeNavigationRoute = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(HomeNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeScreen(
    contentPadding: PaddingValues = PaddingValues()
) {
    composable(
        route = HomeNavigationRoute,
    ) {
        HomeRoute(
            contentPadding = contentPadding,
        )
    }
}
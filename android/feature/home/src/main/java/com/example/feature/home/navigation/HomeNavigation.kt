package com.example.feature.home.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavBackStackEntry
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
    contentPadding: PaddingValues = PaddingValues(),
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
    onNavToSearch:()->Unit = {},
    onNavToWebView:(link:String) -> Unit = {},
    enterTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,
    exitTransition: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,
    popEnterTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?
    )? = enterTransition,
    popExitTransition: (
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?
    )? = exitTransition,
) {
    composable(
        route = HomeNavigationRoute,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
    ) {
        HomeRoute(
            contentPadding = contentPadding,
            onPlaceClick = onPlaceClick,
            onNavToSearch = onNavToSearch,
            onNavToWebView = onNavToWebView
        )
    }
}
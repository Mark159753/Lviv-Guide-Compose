package com.example.search.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.search.ui.SearchRoute
import com.google.accompanist.navigation.animation.composable

const val SearchRoute = "search_route"

fun NavController.navigateToSearchScreen(navOptions: NavOptions? = null) {
    this.navigate(SearchRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.searchScreen(
    onNavBack:()->Unit = {},
    onPlaceClick:(id:Int, color:Int) -> Unit = {_,_ ->},
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
        route = SearchRoute,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
) {
        SearchRoute(
            onNavBack = onNavBack,
            onPlaceClick = onPlaceClick
        )
    }
}
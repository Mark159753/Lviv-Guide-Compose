package com.example.placedetails.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.placedetails.ui.PlaceDetailsRoute
import com.google.accompanist.navigation.animation.composable

const val PlaceDetailsRoute = "place_details_route/{color}/{id}"
const val ColorArg = "color"
const val IdArg = "id"

fun NavController.navigateToPlaceDetails(id:Int, color:Int, navOptions: NavOptions? = null) {
    val route = "place_details_route/$color/$id"
    this.navigate(route, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.placeDetailsScreen(
    onNavBack:()->Unit = {},
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
        route = PlaceDetailsRoute,
        arguments = listOf(
            navArgument(ColorArg) { type = NavType.IntType },
            navArgument(IdArg) { type = NavType.IntType }
        ),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
    ) {
        PlaceDetailsRoute(
            onNavBack = onNavBack
        )
    }
}
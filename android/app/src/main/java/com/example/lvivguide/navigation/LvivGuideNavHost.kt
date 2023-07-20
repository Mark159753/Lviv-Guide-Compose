package com.example.lvivguide.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.account.navigation.accountScreen
import com.example.feature.home.navigation.HomeNavigationRoute
import com.example.feature.home.navigation.homeScreen
import com.example.map.navigation.mapScreen
import com.example.placedetails.navigation.PlaceDetailsRoute
import com.example.placedetails.navigation.navigateToPlaceDetails
import com.example.placedetails.navigation.placeDetailsScreen
import com.example.search.navigation.SearchRoute
import com.example.search.navigation.navigateToSearchScreen
import com.example.search.navigation.searchScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import soup.compose.material.motion.animation.materialSharedAxisXIn
import soup.compose.material.motion.animation.materialSharedAxisXOut
import soup.compose.material.motion.animation.materialSharedAxisZIn
import soup.compose.material.motion.animation.materialSharedAxisZOut

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LvivGuideNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination:String = HomeNavigationRoute,
    contentPadding: PaddingValues = PaddingValues()
){

    AnimatedNavHost(
        navController,
        startDestination = startDestination,
        modifier = modifier
    ){
        homeScreen(
            contentPadding = contentPadding,
            onPlaceClick = { id, color ->
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true){
                    navController.navigateToPlaceDetails(
                        id = id,
                        color = color
                    )
                }
            },
            onNavToSearch = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true){
                    navController.navigateToSearchScreen()
                }
            },
            enterTransition = {
                when(initialState.destination.route){
                    PlaceDetailsRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    SearchRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            exitTransition = {
                when(targetState.destination.route){
                    PlaceDetailsRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    SearchRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popEnterTransition = {
                when(initialState.destination.route){
                    PlaceDetailsRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    SearchRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popExitTransition = {
                when(targetState.destination.route){
                    PlaceDetailsRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    SearchRoute -> materialSharedAxisZOut(forward = true, durationMillis = SharedAxisDuration)
                    else -> null
                }
            }
        )
        mapScreen(
            contentPadding = contentPadding
        )
        accountScreen()

        placeDetailsScreen(
            onNavBack = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true){
                    navController.popBackStack()
                }
            },
            enterTransition = {
                when(initialState.destination.route){
                    SearchRoute,
                    HomeNavigationRoute -> materialSharedAxisXIn(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            exitTransition = {
                when(targetState.destination.route){
                    SearchRoute,
                    HomeNavigationRoute -> materialSharedAxisXOut(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popEnterTransition = {
                when(initialState.destination.route){
                    SearchRoute,
                    HomeNavigationRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popExitTransition = {
                when(targetState.destination.route){
                    SearchRoute,
                    HomeNavigationRoute -> materialSharedAxisXOut(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            }
        )

        searchScreen(
            onNavBack = {
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true){
                    navController.popBackStack()
                }
            },
            onPlaceClick = { id, color ->
                if (navController.currentBackStackEntry?.lifecycleIsResumed() == true){
                    navController.navigateToPlaceDetails(
                        id = id,
                        color = color
                    )
                }
            },

            enterTransition = {
                when(initialState.destination.route){
                    HomeNavigationRoute -> materialSharedAxisZIn(forward = true, durationMillis = SharedAxisDuration)
                    PlaceDetailsRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            exitTransition = {
                when(targetState.destination.route){
                    HomeNavigationRoute -> materialSharedAxisZOut(forward = false, durationMillis = SharedAxisDuration)
                    PlaceDetailsRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popEnterTransition = {
                when(initialState.destination.route){
                    HomeNavigationRoute -> materialSharedAxisZIn(forward = false, durationMillis = SharedAxisDuration)
                    PlaceDetailsRoute -> materialSharedAxisXIn(forward = false, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            },
            popExitTransition = {
                when(targetState.destination.route){
                    HomeNavigationRoute -> materialSharedAxisZOut(forward = false, durationMillis = SharedAxisDuration)
                    PlaceDetailsRoute -> materialSharedAxisXOut(forward = true, slideDistance = SharedAxisSlideDiStance, durationMillis = SharedAxisDuration)
                    else -> null
                }
            }
        )
    }
}

private const val SharedAxisSlideDiStance = 400
private const val SharedAxisDuration = 320

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
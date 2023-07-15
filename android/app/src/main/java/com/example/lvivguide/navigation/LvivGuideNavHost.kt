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
import com.example.placedetails.navigation.navigateToPlaceDetails
import com.example.placedetails.navigation.placeDetailsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

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
            }
        )
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
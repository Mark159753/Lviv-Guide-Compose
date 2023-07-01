package com.example.lvivguide.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.account.navigation.AccountNavigationRoute
import com.example.account.navigation.navigateToAccount
import com.example.feature.home.navigation.HomeNavigationRoute
import com.example.feature.home.navigation.navigateToHome
import com.example.lvivguide.navigation.BottomBarDestinations
import com.example.map.navigation.MapNavigationRoute
import com.example.map.navigation.navigateToMap
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun rememberLvivGuidAppState(
    navController: NavHostController = rememberAnimatedNavController(),
): LvivGuideAppState {
    return remember(
        navController
    ) {
        LvivGuideAppState(
            navController,
        )
    }
}


@Stable
class LvivGuideAppState(
    val navController: NavHostController,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentBottomBarDestination: BottomBarDestinations?
        @Composable get() = when (currentDestination?.route) {
            HomeNavigationRoute -> BottomBarDestinations.HOME
            MapNavigationRoute -> BottomBarDestinations.MAP
            AccountNavigationRoute -> BottomBarDestinations.ACCOUNT
            else -> null
        }

    val bottomBarDestinations: List<BottomBarDestinations> = BottomBarDestinations
        .values().asList()

    fun navigateToBottomBarDestination(bottomBarDestination: BottomBarDestinations) {
        val navOps = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when(bottomBarDestination){
            BottomBarDestinations.HOME -> navController.navigateToHome(navOps)
            BottomBarDestinations.MAP -> navController.navigateToMap(navOps)
            BottomBarDestinations.ACCOUNT -> navController.navigateToAccount(navOps)
        }
    }
}
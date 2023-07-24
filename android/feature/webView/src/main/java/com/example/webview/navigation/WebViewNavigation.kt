package com.example.webview.navigation

import android.net.Uri
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
import com.example.webview.ui.WebViewRoute
import com.google.accompanist.navigation.animation.composable

const val UrlArg = "url"
const val WebViewNavigationRoute = "web_view_route/{$UrlArg}"

fun NavController.navigateToWebViewHome(url:String, navOptions: NavOptions? = null) {
    val encoded =  Uri.encode(url)
    val route = "web_view_route/$encoded"
    this.navigate(route, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.webViewScreen(
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
        route = WebViewNavigationRoute,
        arguments = listOf(
            navArgument(UrlArg) { type = NavType.StringType },
        ),
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popExitTransition = popExitTransition,
        popEnterTransition = popEnterTransition
    ) { backStackEntry ->
        WebViewRoute(
            url = Uri.decode(backStackEntry.arguments?.getString(UrlArg)!!),
            onNavBack = onNavBack
        )
    }
}
package com.example.account.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.example.account.ui.AccountRoute
import com.google.accompanist.navigation.animation.composable

const val AccountNavigationRoute = "account_route"

fun NavController.navigateToAccount(navOptions: NavOptions? = null) {
    this.navigate(AccountNavigationRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.accountScreen() {
    composable(
        route = AccountNavigationRoute,
    ) {
        AccountRoute()
    }
}
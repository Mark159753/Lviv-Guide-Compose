package com.example.lvivguide.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.lvivguide.navigation.BottomBarDestinations
import com.example.lvivguide.navigation.LvivGuideNavHost
import com.example.ui.components.LvivGuideBottomBar
import com.example.ui.components.LvivGuideNavigationBarItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LvivGuideApp(
    appState: LvivGuideAppState = rememberLvivGuidAppState()
){

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            val isVisible = appState.currentBottomBarDestination != null
            AnimatedVisibility(
                visible = isVisible,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 200,
                    ),
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    animationSpec = tween(
                        delayMillis = 50,
                        durationMillis = 200,
                    ),
                    targetOffsetY = { it }
                )
            ) {
                BottomNavBar(
                    destinations = appState.bottomBarDestinations,
                    onNavigateToDestination = appState::navigateToBottomBarDestination,
                    currentDestination = appState.currentDestination
                )
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .consumeWindowInsets(contentPadding)
        ) {
            LvivGuideNavHost(
                navController = appState.navController,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
private fun BottomNavBar(
    destinations: List<BottomBarDestinations>,
    onNavigateToDestination: (BottomBarDestinations) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
){
    LvivGuideBottomBar(
        modifier = modifier
    ){
        destinations.forEach { destination ->
            val selected = currentDestination.isBottomBarDestinationSelected(destination)
            LvivGuideNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.unselectedIconId),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                },
                selectedIcon = {
                    Icon(
                        painter = painterResource(id = destination.selectedIconId),
                        contentDescription = null,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
            )
        }
    }
}

private fun NavDestination?.isBottomBarDestinationSelected(destination: BottomBarDestinations) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
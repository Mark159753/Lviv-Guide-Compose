package com.example.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LvivGuideBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
){
    NavigationBar(
        modifier = modifier,
        contentColor = LvivGuideNavigationDefaults.navigationContentColor(),
        content = content,
        containerColor = LvivGuideNavigationDefaults.navigationContainerColor()
    )
}

@Composable
fun RowScope.LvivGuideNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = LvivGuideNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = LvivGuideNavigationDefaults.navigationContentColor(),
            selectedTextColor = LvivGuideNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = LvivGuideNavigationDefaults.navigationContentColor(),
            indicatorColor = LvivGuideNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

object LvivGuideNavigationDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurface

    @Composable
    fun navigationContainerColor() = MaterialTheme.colorScheme.surface

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primary
}
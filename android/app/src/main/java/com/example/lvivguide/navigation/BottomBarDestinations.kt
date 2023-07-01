package com.example.lvivguide.navigation

import com.example.lvivguide.R

enum class BottomBarDestinations(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
    val titleTextId: Int,
) {

    HOME(
        selectedIconId = R.drawable.home_icon,
        unselectedIconId = R.drawable.home_icon,
        iconTextId = R.string.bottom_nav_home,
        titleTextId = R.string.bottom_nav_home
    ),

    MAP(
        selectedIconId = R.drawable.map_icon,
        unselectedIconId = R.drawable.map_icon,
        iconTextId = R.string.bottom_nav_map,
        titleTextId = R.string.bottom_nav_map
    ),

    ACCOUNT(
        selectedIconId = R.drawable.person_icon,
        unselectedIconId = R.drawable.person_icon,
        iconTextId = R.string.bottom_nav_account,
        titleTextId = R.string.bottom_nav_account
    )
}
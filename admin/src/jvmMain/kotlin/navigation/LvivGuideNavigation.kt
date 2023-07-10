package navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import ui.categories.CategoriesDestination
import ui.home.HomeDestination
import ui.places.PlacesDestination
import ui.places.create.CreatePlaceDestination

@Stable
class NavigationState(
    initialDestination:Screens = Screens.Home
){
    internal var _currentDestination by mutableStateOf(initialDestination)

    val currentDestination:Screens
        get() = _currentDestination

    private val backStack = ArrayDeque<Screens>()

    fun navBack():Boolean{
        val last = backStack.removeLastOrNull()?.let {
            _currentDestination = it
        }
        return last != null
    }

    fun navTo(destination:Screens){
        backStack.addLast(_currentDestination)
        _currentDestination = destination
    }

    companion object {
        internal fun Saver() =
            Saver<NavigationState, Screens>(
                save = { it._currentDestination },
                restore = { NavigationState(it) }
            )
    }
}

@Composable
fun rememberNavigation(initialDestination:Screens):NavigationState{
    return rememberSaveable(saver = NavigationState.Saver()){
        NavigationState(initialDestination)
    }
}

enum class Screens{
    Home,
    Categories,
    Places,
    CreatePlace
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LvivGuideNavigation(
    modifier: Modifier = Modifier,
    navState:NavigationState = rememberNavigation(Screens.Home)
){
    Box(
        modifier
            .fillMaxSize()
    ){
        AnimatedContent(navState._currentDestination){ screen ->
            when(screen){
                Screens.Home -> HomeDestination(
                    navigator = navState
                )
                Screens.Categories -> CategoriesDestination(
                    navigator = navState
                )
                Screens.Places -> PlacesDestination(
                    navigator = navState
                )
                Screens.CreatePlace -> CreatePlaceDestination(
                    navigator = navState
                )
            }
        }
    }
}
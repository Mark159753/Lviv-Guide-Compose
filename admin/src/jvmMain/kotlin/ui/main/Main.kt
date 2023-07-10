package ui.main


import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin
import navigation.LvivGuideNavigation

@Composable
@Preview
fun App() {
    LvivGuideNavigation()
}


fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Lviv Guide Admin"
    ) {
        App()
    }
}

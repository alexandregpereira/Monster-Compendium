import androidx.compose.ui.window.application
import androidx.compose.ui.window.Window
import br.alexandregpereira.hunter.app.di.initKoinModules
import org.koin.core.context.startKoin
import br.alexandregpereira.hunter.app.MainScreen as AppMainScreen

fun main() = application {
    startKoin {
        initKoinModules()
    }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Monster Compendium",
    ) {
        AppMainScreen()
    }
}

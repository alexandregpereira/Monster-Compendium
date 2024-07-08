package br.alexandregpereira.hunter.app

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import br.alexandregpereira.hunter.app.MainScreen as AppMainScreen

class MainViewControllerFactory {

    fun create(): UIViewController = ComposeUIViewController {
        AppMainScreen()
    }
}

package br.alexandregpereira.hunter.app

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

class MainViewControllerFactory {

    fun create(): UIViewController = ComposeUIViewController {
        HunterApp()
    }
}

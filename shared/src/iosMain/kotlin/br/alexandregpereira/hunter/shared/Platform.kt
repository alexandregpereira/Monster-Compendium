package br.alexandregpereira.hunter.shared

import br.alexandregpereira.hunter.shared.di.appModules
import org.koin.core.context.startKoin
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    fun initKoin() {
        startKoin {
            modules(appModules())
        }
    }
}

actual fun getPlatform(): Platform = IOSPlatform()

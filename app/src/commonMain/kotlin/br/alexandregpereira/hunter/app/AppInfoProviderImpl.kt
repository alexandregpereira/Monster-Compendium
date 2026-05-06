package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.config.AppInfoProvider

internal class AppInfoProviderImpl : AppInfoProvider {

    override fun getVersionCode(): Int = AppConfig.VERSION_CODE
}

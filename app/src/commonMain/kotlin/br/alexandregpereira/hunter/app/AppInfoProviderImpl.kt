package br.alexandregpereira.hunter.app

import br.alexandregpereira.hunter.app.config.AppInfoProvider
import br.alexandregpereira.hunter.app.config.Environment

internal class AppInfoProviderImpl(
    private val environment: Environment,
    private val platformName: String,
) : AppInfoProvider {

    override fun getVersionCode(): Int = AppConfig.VERSION_CODE

    override fun getVersionName(): String = AppConfig.VERSION_NAME

    override fun getEnvironment(): Environment = environment

    override fun getPlatformName(): String = platformName
}

package br.alexandregpereira.hunter.app.config

interface AppInfoProvider {

    fun getVersionCode(): Int

    fun getVersionName(): String

    fun getEnvironment(): Environment

    fun getPlatformName(): String
}

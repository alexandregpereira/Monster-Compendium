package br.alexandregpereira.hunter.localization

interface AppLocalization {

    fun getLanguage(): Language
}

interface MutableAppLocalization : AppLocalization {

    fun setLanguage(language: String)
}

internal class AppLocalizationImpl : MutableAppLocalization {

    private var language: Language = getDefaultLanguage()

    override fun getLanguage(): Language {
        return language
    }

    override fun setLanguage(language: String) {
        this.language = Language.entries.firstOrNull { it.code == language } ?: getDefaultLanguage()
    }

    private fun getDefaultLanguage(): Language {
        return Language.entries.firstOrNull {
            it.code == getDeviceLangCode()
        } ?: Language.ENGLISH
    }
}

internal expect fun getDeviceLangCode(): String

enum class Language(val code: String) {
    ENGLISH("en-us"),
    PORTUGUESE("pt-br")
}

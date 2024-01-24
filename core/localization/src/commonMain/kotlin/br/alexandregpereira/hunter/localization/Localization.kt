package br.alexandregpereira.hunter.localization

interface AppLocalization {

    fun getLanguage(): Language
}

interface MutableAppLocalization : AppLocalization {

    fun setLanguage(language: String)
}

internal class AppLocalizationImpl : MutableAppLocalization {

    private var language: Language = Language.ENGLISH

    override fun getLanguage(): Language {
        return language
    }

    override fun setLanguage(language: String) {
        this.language = Language.entries.firstOrNull { it.code == language } ?: Language.ENGLISH
    }
}

enum class Language(val code: String) {
    ENGLISH("en-us"),
    PORTUGUESE("pt-br")
}

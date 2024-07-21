package br.alexandregpereira.hunter.localization

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface AppLocalization {

    fun getLanguage(): Language
}

interface AppReactiveLocalization : AppLocalization {

    val languageFlow: Flow<Language>
}

interface MutableAppLocalization : AppLocalization {

    fun setLanguage(language: String)
}

internal class AppLocalizationImpl(
    private val defaultLanguage: Language,
) : MutableAppLocalization, AppReactiveLocalization {

    private var language: Language = defaultLanguage
    private val _languageFlow: MutableSharedFlow<Language> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val languageFlow: Flow<Language> = _languageFlow.asSharedFlow()

    override fun getLanguage(): Language {
        return language
    }

    override fun setLanguage(language: String) {
        this.language = Language.entries.firstOrNull { it.code == language } ?: defaultLanguage
        _languageFlow.tryEmit(this.language)
    }
}

internal expect fun getDeviceLangCode(): String

enum class Language(val code: String) {
    ENGLISH("en-us"),
    PORTUGUESE("pt-br")
}

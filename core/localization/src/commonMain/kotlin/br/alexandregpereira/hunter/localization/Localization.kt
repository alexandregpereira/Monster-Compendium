/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.localization

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _languageFlow: MutableStateFlow<Language> = MutableStateFlow(
        value = defaultLanguage
    )
    override val languageFlow: Flow<Language> = _languageFlow.asStateFlow()

    override fun getLanguage(): Language {
        return _languageFlow.value
    }

    override fun setLanguage(language: String) {
        val language = Language.entries.firstOrNull { it.code == language } ?: defaultLanguage
        _languageFlow.value = language
    }
}

internal expect fun getDeviceLang(): DeviceLang

internal data class DeviceLang(
    val code: String,
    val language: String,
)

enum class Language(val code: String) {
    ENGLISH("en-us"),
    PORTUGUESE("pt-br"),
    SPANISH("es")
}

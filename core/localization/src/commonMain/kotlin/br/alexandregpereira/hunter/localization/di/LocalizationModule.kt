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

package br.alexandregpereira.hunter.localization.di

import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.localization.AppLocalizationImpl
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.localization.Language
import br.alexandregpereira.hunter.localization.MutableAppLocalization
import br.alexandregpereira.hunter.localization.getDeviceLangCode
import org.koin.dsl.module

val localizationModule = module {
    factory<Language> { getDefaultLanguage() }
    single { AppLocalizationImpl(get()) }
    single<MutableAppLocalization> { get<AppLocalizationImpl>() }
    single<AppLocalization> { get<AppLocalizationImpl>() }
    single<AppReactiveLocalization> { get<AppLocalizationImpl>() }
}

private fun getDefaultLanguage(): Language {
    return Language.entries.firstOrNull {
        it.code == getDeviceLangCode()
    } ?: Language.ENGLISH
}

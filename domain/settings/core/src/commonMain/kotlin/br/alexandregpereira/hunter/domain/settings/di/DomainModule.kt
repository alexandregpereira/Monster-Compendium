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

package br.alexandregpereira.hunter.domain.settings.di

import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.GetAppearanceSettings
import br.alexandregpereira.hunter.domain.settings.GetContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.GetMonsterImageJsonUrlUseCase
import br.alexandregpereira.hunter.domain.settings.SaveAppearanceSettings
import br.alexandregpereira.hunter.domain.settings.SaveContentVersionUseCase
import br.alexandregpereira.hunter.domain.settings.SaveLanguageUseCase
import br.alexandregpereira.hunter.domain.settings.SaveUrlsUseCase
import org.koin.dsl.module

val settingsDomainModule = module {
    factory { GetAlternativeSourceJsonUrlUseCase(get()) }
    factory { GetContentVersionUseCase(get()) }
    factory { GetLanguageUseCase(get(), get(), get()) }
    factory { GetMonsterImageJsonUrlUseCase(get()) }
    factory { SaveContentVersionUseCase(get()) }
    factory { SaveLanguageUseCase(get(), get()) }
    factory { SaveUrlsUseCase(get()) }
    factory { SaveAppearanceSettings(get()) }
    factory { GetAppearanceSettings(get()) }
}

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

package br.alexandregpereira.hunter.data.settings.di

import br.alexandregpereira.hunter.data.settings.DefaultSettingsRepository
import br.alexandregpereira.hunter.data.settings.network.AlternativeSourceUrlBuilder
import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import com.russhwolf.settings.Settings
import org.koin.core.module.Module
import org.koin.dsl.module

val settingsDataModule = module {
    factory { AlternativeSourceUrlBuilder(get()) }
    single<Settings> { get<Settings.Factory>().create("preferences") }
    factory<SettingsRepository> { DefaultSettingsRepository(get()) }
    getAdditionalSettingsModule()
}

internal expect fun Module.getAdditionalSettingsModule()

/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

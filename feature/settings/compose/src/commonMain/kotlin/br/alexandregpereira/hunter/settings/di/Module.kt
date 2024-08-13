/*
 * Copyright 2023 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.settings.di

import br.alexandregpereira.hunter.settings.SettingsAnalytics
import br.alexandregpereira.hunter.settings.SettingsStateHolder
import br.alexandregpereira.hunter.settings.domain.ApplyAppearanceSettings
import br.alexandregpereira.hunter.settings.domain.GetAppearanceSettingsFromMonsters
import org.koin.dsl.module

val featureSettingsModule = module {
    factory { GetAppearanceSettingsFromMonsters(get(), get()) }
    factory { ApplyAppearanceSettings(get(), get(), get(), get()) }
    single {
        SettingsStateHolder(
            getMonsterImageJsonUrl = get(),
            getAlternativeSourceJsonUrl = get(),
            saveUrls = get(),
            monsterContentManagerEventDispatcher = get(),
            dispatcher = get(),
            syncEventDispatcher = get(),
            analytics = SettingsAnalytics(get()),
            appLocalization = get(),
            saveLanguage = get(),
            applyAppearanceSettings = get(),
            getAppearanceSettings = get(),
            monsterEventDispatcher = get(),
            shareContentEventDispatcher = get(),
        )
    }
}

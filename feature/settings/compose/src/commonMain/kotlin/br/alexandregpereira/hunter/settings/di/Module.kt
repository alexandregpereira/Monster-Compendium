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

package br.alexandregpereira.hunter.settings.di

import br.alexandregpereira.hunter.paywall.event.PaywallEventDispatcher
import br.alexandregpereira.hunter.paywall.event.PaywallResultDispatcher
import br.alexandregpereira.hunter.settings.SettingsAnalytics
import br.alexandregpereira.hunter.settings.SettingsStateHolder
import br.alexandregpereira.hunter.settings.domain.ApplyAppearanceSettings
import br.alexandregpereira.hunter.settings.domain.GetAppearanceSettingsFromMonsters
import br.alexandregpereira.hunter.settings.domain.IsManageContentFeatureEnabled
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import org.koin.dsl.module

val featureSettingsModule = module {
    factory { GetAppearanceSettingsFromMonsters(get(), get()) }
    factory { ApplyAppearanceSettings(get(), get(), get(), get()) }
    factory {
        IsManageContentFeatureEnabled(
            featureFlagProvider = get(),
            alternativeSourceLocalRepository = get(),
        )
    }
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
            paywallEventDispatcher = get<PaywallEventDispatcher>(),
            isSessionUsageLimitReached = get(),
            paywallResultListener = get<PaywallResultDispatcher>(),
            spellCompendiumEventDispatcher = get<SpellCompendiumEventResultDispatcher>(),
            spellDetailEventDispatcher = get(),
            spellRegistrationEventDispatcher = get<SpellRegistrationEventDispatcher>(),
            isManageContentFeatureEnabled = get(),
            environment = get(),
        )
    }
}

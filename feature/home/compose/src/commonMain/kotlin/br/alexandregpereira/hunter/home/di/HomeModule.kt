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

package br.alexandregpereira.hunter.home.di

import br.alexandregpereira.hunter.event.folder.list.FolderListEventDispatcher
import br.alexandregpereira.hunter.home.HomeEventDispatcher
import br.alexandregpereira.hunter.home.HomeEventDispatcherImpl
import br.alexandregpereira.hunter.home.HomeStateHolder
import br.alexandregpereira.hunter.monster.compendium.event.MonsterCompendiumEventDispatcher
import br.alexandregpereira.hunter.search.event.SearchEventDispatcher
import br.alexandregpereira.hunter.settings.event.SettingsEventDispatcher
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEventResultDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import org.koin.dsl.module

val featureHomeModule = module {
    single {
        HomeStateHolder(
            appLocalization = get(),
            homeEventDispatcher = get(),
        )
    }
    factory<HomeEventDispatcher> {
        HomeEventDispatcherImpl(
            monsterCompendiumEventDispatcher = get<MonsterCompendiumEventDispatcher>(),
            spellCompendiumEventDispatcher = get<SpellCompendiumEventResultDispatcher>(),
            spellDetailEventDispatcher = get(),
            spellRegistrationEventDispatcher = get<SpellRegistrationEventDispatcher>(),
            searchEventDispatcher = get<SearchEventDispatcher>(),
            settingsEventDispatcher = get<SettingsEventDispatcher>(),
            folderListEventDispatcher = get<FolderListEventDispatcher>(),
            folderDetailEventDispatcher = get(),
        )
    }
}

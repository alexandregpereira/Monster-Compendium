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

package br.alexandregpereira.hunter.domain.di

import br.alexandregpereira.hunter.domain.folder.di.monsterFolderDomainModule
import br.alexandregpereira.hunter.domain.monster.lore.di.monsterLoreDomainModule
import br.alexandregpereira.hunter.domain.settings.di.settingsDomainModule
import br.alexandregpereira.hunter.domain.source.di.alternativeSourceDomainModule
import br.alexandregpereira.hunter.domain.spell.di.spellDomainModule
import br.alexandregpereira.hunter.domain.sync.di.syncDomainModule
import br.alexandregpereira.hunter.monster.compendium.domain.di.monsterCompendiumDomainModule

val domainModules = listOf(
    alternativeSourceDomainModule,
    monsterDomainModule,
    monsterCompendiumDomainModule,
    monsterFolderDomainModule,
    monsterLoreDomainModule,
    settingsDomainModule,
    spellDomainModule,
    syncDomainModule
)

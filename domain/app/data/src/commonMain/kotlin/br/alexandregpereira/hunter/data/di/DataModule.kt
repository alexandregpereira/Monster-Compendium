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

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.monster.di.monsterDataModule
import br.alexandregpereira.hunter.data.monster.folder.di.monsterFolderDataModule
import br.alexandregpereira.hunter.data.monster.lore.di.monsterLoreDataModule
import br.alexandregpereira.hunter.data.settings.di.settingsDataModule
import br.alexandregpereira.hunter.data.source.di.alternativeSourceDataModule
import br.alexandregpereira.hunter.data.spell.di.spellDataModule
import org.koin.core.module.Module

fun dataModules(databaseName: String) = listOf(
    networkModule,
    databaseModule(databaseName),
    alternativeSourceDataModule,
    monsterDataModule,
    monsterFolderDataModule,
    monsterLoreDataModule,
    settingsDataModule,
    spellDataModule,
).run { this + getAdditionalModule() }

internal expect fun getAdditionalModule(): List<Module>

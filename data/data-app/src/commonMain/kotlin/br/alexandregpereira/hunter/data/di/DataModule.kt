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

package br.alexandregpereira.hunter.data.di

import br.alexandregpereira.hunter.data.monster.di.monsterDataModule
import br.alexandregpereira.hunter.data.monster.folder.di.monsterFolderDataModule
import br.alexandregpereira.hunter.data.monster.lore.di.monsterLoreDataModule
import br.alexandregpereira.hunter.data.settings.di.settingsDataModule
import br.alexandregpereira.hunter.data.source.di.alternativeSourceDataModule
import br.alexandregpereira.hunter.data.spell.di.spellDataModule
import org.koin.core.module.Module

val dataModules = listOf(
    alternativeSourceDataModule,
    monsterDataModule,
    monsterFolderDataModule,
    monsterLoreDataModule,
    settingsDataModule,
    spellDataModule
).run { this + getAdditionalModule() }

internal expect fun getAdditionalModule(): List<Module>

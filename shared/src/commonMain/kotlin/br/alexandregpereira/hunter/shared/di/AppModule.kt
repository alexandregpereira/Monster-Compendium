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

package br.alexandregpereira.hunter.shared.di

import br.alexandregpereira.hunter.data.di.dataModules
import br.alexandregpereira.hunter.domain.di.domainModules
import br.alexandregpereira.hunter.event.folder.insert.emptyFolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.emptyMonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.emptyFolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.emptyFolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.state.di.monsterCompendiumStateModule
import br.alexandregpereira.hunter.monster.detail.di.monsterDetailStateModule
import br.alexandregpereira.hunter.spell.detail.event.emptySpellDetailEventDispatcher
import br.alexandregpereira.hunter.sync.di.syncStateModule
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModules(): List<Module> = domainModules + dataModules +
        monsterCompendiumStateModule +
        monsterDetailStateModule +
        syncStateModule +
        module {
            factory { Dispatchers.Default }
            factory {
                emptySpellDetailEventDispatcher()
            }
            factory {
                emptyMonsterLoreDetailEventDispatcher()
            }
            factory {
                emptyFolderInsertEventDispatcher()
            }
            factory {
                emptyFolderPreviewResultListener()
            }
            factory {
                emptyFolderPreviewEventDispatcher()
            }
        }

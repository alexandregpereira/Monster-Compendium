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

package br.alexandregpereira.hunter.shared

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.sync.SyncStateHolder
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MonsterCompendiumFeature : KoinComponent {

    val stateHolder: MonsterCompendiumStateHolder by inject()

    // TODO Remove after iOS sync feature implementation
    val syncEventDispatcher: SyncEventDispatcher by inject()
    val syncStateHolder: SyncStateHolder by inject()

    init {
        // TODO Remove after iOS sync feature implementation
        syncStateHolder.state.iosFlow(stateHolder.scope).collect {
            println("Sync state $it")
        }
    }

    val state: IosFlow<MonsterCompendiumStateIos> = stateHolder.state.map {
        it.asMonsterCompendiumStateIos()
    }.onEach { monsters ->
        // TODO Remove after iOS sync feature implementation
        if (monsters.isLoading.not() && monsters.items.isEmpty()) {
            syncEventDispatcher.startSync()
        }
    }.iosFlow(stateHolder.scope)

    val action: IosFlow<MonsterCompendiumActionIos> = stateHolder.action.map { action ->
        when (action) {
            is MonsterCompendiumAction.GoToCompendiumIndex -> MonsterCompendiumActionIos(
                compendiumIndex = action.index
            )
        }
    }.iosFlow(stateHolder.scope)

    private fun MonsterCompendiumState.asMonsterCompendiumStateIos(): MonsterCompendiumStateIos {
        return MonsterCompendiumStateIos(
            isLoading = isLoading,
            items = items.map { it.asMonsterCompendiumItemIos() },
            alphabet = alphabet,
            alphabetSelectedIndex = alphabetSelectedIndex,
            popupOpened = popupOpened,
            tableContent = tableContent,
            tableContentIndex = tableContentIndex,
            tableContentInitialIndex = tableContentInitialIndex,
            tableContentOpened = tableContentOpened,
            isShowingMonsterFolderPreview = isShowingMonsterFolderPreview,
        )
    }

    private fun MonsterCompendiumItem.asMonsterCompendiumItemIos(): MonsterCompendiumItemIos {
        return MonsterCompendiumItemIos(
            title = this as? MonsterCompendiumItem.Title,
            monster = (this as? MonsterCompendiumItem.Item)?.monster
        )
    }
}

@ObjCName(name = "MonsterCompendiumStateIos", exact = true)
data class MonsterCompendiumStateIos(
    val isLoading: Boolean = false,
    val items: List<MonsterCompendiumItemIos> = emptyList(),
    val alphabet: List<String> = emptyList(),
    val alphabetSelectedIndex: Int = -1,
    val popupOpened: Boolean = false,
    val tableContent: List<TableContentItem> = emptyList(),
    val tableContentIndex: Int = -1,
    val tableContentInitialIndex: Int = 0,
    val tableContentOpened: Boolean = false,
    val isShowingMonsterFolderPreview: Boolean = false,
)

@ObjCName(name = "MonsterCompendiumItemIos", exact = true)
data class MonsterCompendiumItemIos(
    val title: MonsterCompendiumItem.Title?,
    val monster: Monster?
)

@ObjCName(name = "MonsterCompendiumActionIos", exact = true)
data class MonsterCompendiumActionIos(
    val compendiumIndex: Int?
)

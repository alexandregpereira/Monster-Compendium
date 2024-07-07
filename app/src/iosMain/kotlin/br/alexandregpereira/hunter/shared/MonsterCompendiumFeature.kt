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

import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumItemState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.state.MonsterPreviewState
import br.alexandregpereira.hunter.sync.SyncStateHolder
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MonsterCompendiumFeature : KoinComponent {

    val stateHolder: MonsterCompendiumStateHolder by inject()

    // TODO Remove after iOS sync feature implementation
    private val syncEventDispatcher: SyncEventDispatcher by inject()
    private val syncStateHolder: SyncStateHolder by inject()

    init {
        // TODO Remove after iOS sync feature implementation
        syncStateHolder.state.subscribe {
            println("Sync state $it")
        }

        stateHolder.state.subscribe { monsters ->
            // TODO Remove after iOS sync feature implementation
            if (monsters.isLoading.not() && monsters.items.isEmpty()) {
                syncEventDispatcher.startSync()
            }
        }
    }
}

/**
 * For some reason the compiler do not generate these sealed classes if I don't do this.
 * Seems that it needs to use directly the sealed class types to be generated.
 **/
val stubAction = MonsterCompendiumAction.GoToCompendiumIndex(0)
val stubItem = MonsterCompendiumItemState.Item(
    monster = MonsterPreviewState(
        index = "",
        name = "",
        type = MonsterType.PLANT,
        challengeRating = "0",
        imageUrl = "",
        backgroundColorLight = "",
        backgroundColorDark = "",
        isImageHorizontal = false
    )
)
val stubTitle = MonsterCompendiumItemState.Title(
    id = "",
    value = "",
    isHeader = false
)

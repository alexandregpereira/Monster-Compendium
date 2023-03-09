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
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MonsterCompendiumFeature : KoinComponent {

    private val stateHolder: MonsterCompendiumStateHolder by inject()

    val state: CFlow<MonsterCompendiumStateIos> = stateHolder.state.map {
        it.asMonsterCompendiumStateIos()
    }.wrap()

    fun onItemClick(index: String) {
        stateHolder.onItemCLick(index)
    }

    fun onFirstVisibleItemChange(position: Int) {
        stateHolder.onFirstVisibleItemChange(position)
    }

    fun onPopupOpened() {
        stateHolder.onPopupOpened()
    }

    fun onPopupClosed() {
        stateHolder.onPopupClosed()
    }

    fun onAlphabetIndexClicked(position: Int) {
        stateHolder.onAlphabetIndexClicked(position)
    }

    fun onTableContentIndexClicked(position: Int) {
        stateHolder.onTableContentIndexClicked(position)
    }

    fun onTableContentClosed() {
        stateHolder.onTableContentClosed()
    }

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

data class MonsterCompendiumItemIos(
    val title: MonsterCompendiumItem.Title?,
    val monster: Monster?
)

/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.content.preview

import br.alexandregpereira.hunter.monster.compendium.domain.model.MonsterCompendiumItem
import br.alexandregpereira.hunter.monster.compendium.domain.model.TableContentItem

data class MonsterContentPreviewState(
    val title: String = "",
    val monsterCompendiumItems: List<MonsterCompendiumItem> = emptyList(),
    val tableContent: List<TableContentItem> = emptyList(),
    val alphabet: List<String> = emptyList(),
    val alphabetSelectedIndex: Int = -1,
    val tableContentSelectedIndex: Int = -1,
    val tableContentOpened: Boolean = false,
    val isOpen: Boolean = false,
    val isLoading: Boolean = true,
)

internal fun MonsterContentPreviewState.hide(): MonsterContentPreviewState {
    return copy(isOpen = false)
}

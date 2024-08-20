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

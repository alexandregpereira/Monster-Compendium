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

data class MonsterContentPreviewAction(
    val index: Int,
    val type: MonsterContentPreviewActionType,
) {
    companion object {
        fun goToCompendiumIndex(index: Int): MonsterContentPreviewAction {
            return MonsterContentPreviewAction(
                index = index,
                type = MonsterContentPreviewActionType.GO_TO_COMPENDIUM_INDEX
            )
        }
    }
}

enum class MonsterContentPreviewActionType {
    GO_TO_COMPENDIUM_INDEX,
}

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

package br.alexandregpereira.hunter.monster.lore.detail

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore

data class MonsterLoreDetailState(
    val monsterLore: MonsterLore? = null,
    val showDetail: Boolean = false,
)

internal fun MonsterLoreDetailState.changeMonsterLore(monsterLore: MonsterLore): MonsterLoreDetailState {
    return copy(monsterLore = monsterLore, showDetail = true)
}

internal fun MonsterLoreDetailState.hideDetail(): MonsterLoreDetailState {
    return copy(showDetail = false)
}

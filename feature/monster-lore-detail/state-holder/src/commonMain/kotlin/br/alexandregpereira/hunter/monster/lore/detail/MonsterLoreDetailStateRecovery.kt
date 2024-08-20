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

import br.alexandregpereira.hunter.ui.StateRecovery

internal val StateRecovery.monsterLoreIndex: String
    get() = this["monsterLoreDetail:monsterLoreIndex"] as? String ?: ""

internal fun StateRecovery.saveMonsterLoreIndex(monsterLoreIndex: String) {
    this["monsterLoreDetail:monsterLoreIndex"] = monsterLoreIndex
    dispatchChanges()
}

internal val Map<String, Any?>.showDetail: Boolean
    get() = this["monsterLoreDetail:showDetail"] as? Boolean ?: false

internal fun MonsterLoreDetailState.saveState(
    stateRecovery: StateRecovery
): MonsterLoreDetailState {
    stateRecovery["monsterLoreDetail:showDetail"] = showDetail
    stateRecovery.dispatchChanges()
    return this
}

internal fun StateRecovery.getState(): MonsterLoreDetailState {
    return MonsterLoreDetailState(
        showDetail = this.showDetail,
    )
}

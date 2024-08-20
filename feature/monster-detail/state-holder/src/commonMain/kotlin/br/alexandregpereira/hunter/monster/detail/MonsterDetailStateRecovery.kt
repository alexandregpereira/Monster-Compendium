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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.ui.StateRecovery

internal val StateRecovery.monsterIndex: String
    get() = this["monsterDetail:monsterIndex"] as? String ?: ""

internal val StateRecovery.monsterIndexes: List<String>
    get() = (this["monsterDetail:monsterIndexes"] as? List<*>)?.map { it as String } ?: emptyList()

internal fun StateRecovery.saveMonsterIndex(monsterIndex: String) {
    this["monsterDetail:monsterIndex"] = monsterIndex
    dispatchChanges()
}

internal fun StateRecovery.saveMonsterIndexes(monsterIndexes: List<String>) {
    this["monsterDetail:monsterIndexes"] = monsterIndexes
    dispatchChanges()
}

internal fun MonsterDetailState.saveState(stateRecovery: StateRecovery): MonsterDetailState {
    stateRecovery["monsterDetail:showDetail"] = showDetail
    stateRecovery["monsterDetail:showCloneForm"] = showCloneForm
    stateRecovery["monsterDetail:monsterCloneName"] = monsterCloneName
    stateRecovery.dispatchChanges()
    return this
}

internal fun MonsterDetailState.updateState(bundle: Map<String, Any?>): MonsterDetailState {
    return copy(
        showDetail = bundle["monsterDetail:showDetail"] as? Boolean ?: false,
        showCloneForm = bundle["monsterDetail:showCloneForm"] as? Boolean ?: false,
        monsterCloneName = bundle["monsterDetail:monsterCloneName"] as? String ?: ""
    )
}

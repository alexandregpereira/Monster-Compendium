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

import br.alexandregpereira.hunter.ui.StateRecovery

internal fun StateRecovery.getState(): MonsterContentPreviewState {
    return MonsterContentPreviewState(
        title = this.title,
        isOpen = this["monsterContentPreview:isOpen"] as? Boolean ?: false,
    )
}

internal fun MonsterContentPreviewState.saveState(
    stateRecovery: StateRecovery
): MonsterContentPreviewState {
    stateRecovery["monsterContentPreview:title"] = title
    stateRecovery["monsterContentPreview:isOpen"] = isOpen
    stateRecovery.dispatchChanges()
    return this
}

internal val StateRecovery.title: String
    get() = this["monsterContentPreview:title"] as? String ?: ""

internal var StateRecovery.sourceAcronym: String
    get() = this["monsterContentPreview:sourceAcronym"] as? String ?: ""
    set(value) {
        this["monsterContentPreview:sourceAcronym"] = value
        dispatchChanges()
    }

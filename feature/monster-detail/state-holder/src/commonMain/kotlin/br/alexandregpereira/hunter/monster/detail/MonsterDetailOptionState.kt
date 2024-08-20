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

import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.CLONE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.DELETE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EDIT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EXPORT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.RESET_TO_ORIGINAL
import kotlin.native.ObjCName

@ObjCName(name = "MonsterDetailOptionState", exact = true)
data class MonsterDetailOptionState(
    val id: MonsterDetailOptionStateId = ADD_TO_FOLDER,
    val name: String = "",
) {

    companion object {
        @Suppress("FunctionName")
        internal fun Export(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = EXPORT,
                name = strings.export
            )
        }

        @Suppress("FunctionName")
        internal fun ResetToOriginal(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = RESET_TO_ORIGINAL,
                name = strings.resetToOriginal
            )
        }

        @Suppress("FunctionName")
        internal fun AddToFolder(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = ADD_TO_FOLDER,
                name = strings.optionsAddToFolder
            )
        }

        @Suppress("FunctionName")
        internal fun Clone(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = CLONE,
                name = strings.clone
            )
        }

        @Suppress("FunctionName")
        internal fun Edit(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = EDIT,
                name = strings.edit
            )
        }

        @Suppress("FunctionName")
        internal fun Delete(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = DELETE,
                name = strings.delete
            )
        }
    }
}

enum class MonsterDetailOptionStateId {
    ADD_TO_FOLDER,
    CLONE,
    EDIT,
    DELETE,
    RESET_TO_ORIGINAL,
    EXPORT,
}

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

package br.alexandregpereira.hunter.monster.detail

import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.ADD_LORE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.CLONE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.DELETE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EDIT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EDIT_LORE
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
        internal fun EditLore(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = EDIT_LORE,
                name = strings.editLore
            )
        }

        @Suppress("FunctionName")
        internal fun AddLore(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = ADD_LORE,
                name = strings.addLore
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
    EDIT_LORE,
    ADD_LORE,
    DELETE,
    RESET_TO_ORIGINAL,
    EXPORT,
}

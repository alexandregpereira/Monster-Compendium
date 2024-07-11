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

import kotlin.native.ObjCName

@ObjCName(name = "MonsterDetailOptionState", exact = true)
data class MonsterDetailOptionState(
    val id: String = "",
    val name: String = "",
) {

    companion object {
        internal const val ADD_TO_FOLDER = "add_to_folder"
        internal const val CLONE = "clone"
        internal const val EDIT = "edit"
        internal const val DELETE = "delete"
        internal const val CHANGE_TO_FEET = "change_to_feet"
        internal const val CHANGE_TO_METERS = "change_to_meters"
        internal const val RESET_TO_ORIGINAL = "reset_to_original"

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

        @Suppress("FunctionName")
        internal fun ChangeToFeet(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = CHANGE_TO_FEET,
                name = strings.optionsChangeToFeet
            )
        }

        @Suppress("FunctionName")
        internal fun ChangeToMeters(strings: MonsterDetailStrings): MonsterDetailOptionState {
            return MonsterDetailOptionState(
                id = CHANGE_TO_METERS,
                name = strings.optionsChangeToMeters
            )
        }
    }
}

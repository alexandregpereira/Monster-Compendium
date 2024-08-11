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

package br.alexandregpereira.hunter.monster.content

data class MonsterContentManagerState(
    val monsterContents: List<MonsterContentState> = emptyList(),
    val isOpen: Boolean = false,
    val isLoading: Boolean = false,
    val strings: MonsterContentManagerStrings = MonsterContentManagerEmptyStrings(),
)

data class MonsterContentState(
    val acronym: String,
    val name: String,
    val originalName: String?,
    val totalMonsters: Int,
    val summary: String,
    val coverImageUrl: String,
    val isEnabled: Boolean,
)

internal fun MonsterContentManagerState.hide(): MonsterContentManagerState {
    return copy(isOpen = false)
}

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

import br.alexandregpereira.hunter.domain.model.Monster
import kotlin.native.ObjCName

@ObjCName(name = "MonsterDetailState", exact = true)
data class MonsterDetailState(
    val isLoading: Boolean = true,
    val initialMonsterListPositionIndex: Int = 0,
    val monsters: List<Monster> = emptyList(),
    val showOptions: Boolean = false,
    val options: List<MonsterDetailOptionState> = emptyList(),
    val showDetail: Boolean = false,
    val showCloneForm: Boolean = false,
    val monsterCloneName: String = "",
)

val MonsterDetailState.ShowOptions: MonsterDetailState
    get() = this.copy(showOptions = true)

val MonsterDetailState.HideOptions: MonsterDetailState
    get() = this.copy(showOptions = false)

fun MonsterDetailState.complete(
    initialMonsterListPositionIndex: Int,
    monsters: List<Monster>,
    options: List<MonsterDetailOptionState>
): MonsterDetailState = this.copy(
    isLoading = false,
    initialMonsterListPositionIndex = initialMonsterListPositionIndex,
    monsters = monsters,
    options = options
)

fun MonsterDetailState.showCloneForm(): MonsterDetailState = this.copy(
    showCloneForm = true,
    monsterCloneName = "",
)

fun MonsterDetailState.hideCloneForm(): MonsterDetailState = this.copy(showCloneForm = false)

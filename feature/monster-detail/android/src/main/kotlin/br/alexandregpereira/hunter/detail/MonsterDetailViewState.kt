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

package br.alexandregpereira.hunter.detail

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.monster.detail.MonsterDetailState

fun SavedStateHandle.getState(): MonsterDetailState {
    return MonsterDetailState(
        showDetail = this["showDetail"] ?: false,
        showCloneForm = this["showCloneForm"] ?: false,
        monsterCloneName = this["monsterCloneName"] ?: "",
    )
}

internal fun SavedStateHandle.getMonsterIndex(): String {
    return this["index"] ?: ""
}

internal fun SavedStateHandle.saveMonsterIndex(index: String) {
    this["index"] = index
}

internal fun SavedStateHandle.getMonsterIndexes(): List<String> {
    return this["indexes"] ?: emptyList()
}


internal fun SavedStateHandle.saveMonsterIndexes(indexes: List<String>) {
    this["indexes"] = indexes
}

fun MonsterDetailState.saveState(savedStateHandle: SavedStateHandle): MonsterDetailState {
    savedStateHandle["showDetail"] = showDetail
    savedStateHandle["showCloneForm"] = showCloneForm
    savedStateHandle["monsterCloneName"] = monsterCloneName
    return this
}

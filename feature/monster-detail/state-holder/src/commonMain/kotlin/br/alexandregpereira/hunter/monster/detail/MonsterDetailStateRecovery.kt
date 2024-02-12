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

interface MonsterDetailStateRecovery {
    val state: MonsterDetailState
    val monsterIndex: String
    val monsterIndexes: List<String>

    fun saveState(state: MonsterDetailState)

    fun saveMonsterIndex(index: String)

    fun saveMonsterIndexes(indexes: List<String>)
}

fun MonsterDetailStateRecovery(): MonsterDetailStateRecovery = DefaultMonsterDetailStateRecovery()

internal fun MonsterDetailState.saveState(
    recovery: MonsterDetailStateRecovery
): MonsterDetailState {
    recovery.saveState(this)
    return this
}

private class DefaultMonsterDetailStateRecovery : MonsterDetailStateRecovery {

    override val state: MonsterDetailState = MonsterDetailState.Empty

    private var _monsterIndex: String = ""
    override val monsterIndex: String
        get() = _monsterIndex

    private var _monsterIndexes: List<String> = emptyList()
    override val monsterIndexes: List<String>
        get() = _monsterIndexes

    override fun saveState(state: MonsterDetailState) {}

    override fun saveMonsterIndex(index: String) {
        _monsterIndex = index
    }

    override fun saveMonsterIndexes(indexes: List<String>) {
        _monsterIndexes = indexes
    }
}

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

internal fun MonsterDetailState.saveState(
    recovery: MonsterDetailStateRecovery
): MonsterDetailState {
    recovery.saveState(this)
    return this
}

internal class EmptyMonsterDetailStateRecovery : MonsterDetailStateRecovery {
    override val state: MonsterDetailState = MonsterDetailState()
    override val monsterIndex: String = ""
    override val monsterIndexes: List<String> = emptyList()

    override fun saveState(state: MonsterDetailState) {}

    override fun saveMonsterIndex(index: String) {}

    override fun saveMonsterIndexes(indexes: List<String>) {}
}

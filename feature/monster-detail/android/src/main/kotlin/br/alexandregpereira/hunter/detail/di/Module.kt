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

package br.alexandregpereira.hunter.detail.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.detail.MonsterDetailViewModel
import br.alexandregpereira.hunter.detail.getMonsterIndex
import br.alexandregpereira.hunter.detail.getMonsterIndexes
import br.alexandregpereira.hunter.detail.getState
import br.alexandregpereira.hunter.detail.saveMonsterIndex
import br.alexandregpereira.hunter.detail.saveMonsterIndexes
import br.alexandregpereira.hunter.detail.saveState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateRecovery
import br.alexandregpereira.hunter.monster.detail.di.monsterDetailStateModule
import org.koin.androidx.viewmodel.dsl.viewModel

val monsterDetailModule = monsterDetailStateModule.apply {
    factory<MonsterDetailStateRecovery> {
        AndroidMonsterDetailStateRecovery(
            savedStateHandle = get(),
        )
    }
    viewModel {
        MonsterDetailViewModel(
            stateHolder = get(),
        )
    }
}

private class AndroidMonsterDetailStateRecovery(
    private val savedStateHandle: SavedStateHandle,
) : MonsterDetailStateRecovery {

    override val state: MonsterDetailState
        get() = savedStateHandle.getState()

    override val monsterIndex: String
        get() = savedStateHandle.getMonsterIndex()

    override val monsterIndexes: List<String>
        get() = savedStateHandle.getMonsterIndexes()

    override fun saveState(state: MonsterDetailState) {
        state.saveState(savedStateHandle)
    }

    override fun saveMonsterIndex(index: String) {
        savedStateHandle.saveMonsterIndex(index)
    }

    override fun saveMonsterIndexes(indexes: List<String>) {
        savedStateHandle.saveMonsterIndexes(indexes)
    }
}

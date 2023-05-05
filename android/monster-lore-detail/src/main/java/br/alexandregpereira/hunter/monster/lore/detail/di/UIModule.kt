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

package br.alexandregpereira.hunter.monster.lore.detail.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailState
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailStateRecovery
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreDetailViewModel
import br.alexandregpereira.hunter.monster.lore.detail.MonsterLoreIndexStateRecovery
import br.alexandregpereira.hunter.monster.lore.detail.getState
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val monsterLoreDetailModule = listOf(featureMonsterLoreDetailModule) + module {
    factory {
        val savedStateHandle = get<SavedStateHandle>()
        MonsterLoreDetailStateRecovery {
            val state = savedStateHandle.getState()
            MonsterLoreDetailState(
                showDetail = state.showDetail,
            )
        }
    }

    factory<MonsterLoreIndexStateRecovery> {
        AndroidMonsterLoreIndexStateRecovery(get())
    }

    viewModel {
        MonsterLoreDetailViewModel(get(), get())
    }
}

private class AndroidMonsterLoreIndexStateRecovery(
    private val savedStateHandle: SavedStateHandle
) : MonsterLoreIndexStateRecovery {

    override fun getState(): String = savedStateHandle["monsterLoreIndex"] ?: ""

    override fun saveState(value: String) {
        savedStateHandle["monsterLoreIndex"] = value
    }
}

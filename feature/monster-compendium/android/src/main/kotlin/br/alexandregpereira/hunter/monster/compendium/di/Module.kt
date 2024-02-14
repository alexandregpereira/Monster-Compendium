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

package br.alexandregpereira.hunter.monster.compendium.di

import androidx.lifecycle.SavedStateHandle
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewModel
import br.alexandregpereira.hunter.monster.compendium.getState
import br.alexandregpereira.hunter.monster.compendium.saveState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateRecovery
import br.alexandregpereira.hunter.monster.compendium.state.di.monsterCompendiumStateModule
import org.koin.androidx.viewmodel.dsl.viewModel

val monsterCompendiumModule = monsterCompendiumStateModule.apply {
    factory<MonsterCompendiumStateRecovery> {
        AndroidMonsterCompendiumStateRecovery(
            savedStateHandle = get(),
        )
    }
    viewModel {
        MonsterCompendiumViewModel(
            stateHolder = get(),
        )
    }
}

private class AndroidMonsterCompendiumStateRecovery(
    private val savedStateHandle: SavedStateHandle,
) : MonsterCompendiumStateRecovery {

    override val state: MonsterCompendiumState
        get() = savedStateHandle.getState()

    override fun saveState(state: MonsterCompendiumState) {
        state.saveState(savedStateHandle)
    }
}

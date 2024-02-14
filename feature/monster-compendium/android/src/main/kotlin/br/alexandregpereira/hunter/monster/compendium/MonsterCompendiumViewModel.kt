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

package br.alexandregpereira.hunter.monster.compendium

import androidx.lifecycle.ViewModel
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumIntent
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.state.ActionHandler
import br.alexandregpereira.hunter.state.StateHolder

internal class MonsterCompendiumViewModel(
    private val stateHolder: MonsterCompendiumStateHolder,
) : ViewModel(),
    StateHolder<MonsterCompendiumState> by stateHolder,
    ActionHandler<MonsterCompendiumAction> by stateHolder,
    MonsterCompendiumIntent by stateHolder {

    val initialScrollItemPosition: Int
        get() = stateHolder.initialScrollItemPosition

    fun loadMonsters() = stateHolder.loadMonsters()

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}

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

import androidx.lifecycle.ViewModel
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.state.StateHolder

internal class MonsterDetailViewModel(
    private val stateHolder: MonsterDetailStateHolder,
) : ViewModel(), StateHolder<MonsterDetailState> by stateHolder {

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        stateHolder.onMonsterChanged(monsterIndex, scrolled)
    }

    fun onShowOptionsClicked() = stateHolder.onShowOptionsClicked()

    fun onShowOptionsClosed() = stateHolder.onShowOptionsClosed()

    fun onOptionClicked(option: MonsterDetailOptionState) {
        stateHolder.onOptionClicked(option)
    }

    fun onSpellClicked(spellIndex: String) {
        stateHolder.onSpellClicked(spellIndex)
    }

    fun onLoreClicked(monsterIndex: String) {
        stateHolder.onLoreClicked(monsterIndex)
    }

    fun onClose() {
        stateHolder.onClose()
    }

    fun onCloneFormClosed() = stateHolder.onCloneFormClosed()

    fun onCloneFormChanged(monsterName: String) {
        stateHolder.onCloneFormChanged(monsterName)
    }

    fun onCloneFormSaved() = stateHolder.onCloneFormSaved()

    fun onDeleteConfirmed() = stateHolder.onDeleteConfirmed()

    fun onDeleteClosed() = stateHolder.onDeleteClosed()

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }
}

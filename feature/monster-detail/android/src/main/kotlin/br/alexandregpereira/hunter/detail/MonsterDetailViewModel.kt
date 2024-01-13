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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.monster.detail.MonsterDetailAnalytics
import br.alexandregpereira.hunter.monster.detail.MonsterDetailState
import br.alexandregpereira.hunter.monster.detail.MonsterDetailStateHolder
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonsterDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    getMonsterDetailUseCase: GetMonsterDetailUseCase,
    cloneMonster: CloneMonsterUseCase,
    changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    deleteMonsterUseCase: DeleteMonsterUseCase,
    spellDetailEventDispatcher: SpellDetailEventDispatcher,
    monsterDetailEventListener: MonsterDetailEventListener,
    monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    folderInsertEventDispatcher: FolderInsertEventDispatcher,
    monsterRegistrationEventDispatcher: EventDispatcher<MonsterRegistrationEvent>,
    monsterRegistrationEventListener: EventListener<MonsterRegistrationResult>,
    dispatcher: CoroutineDispatcher,
    analytics: MonsterDetailAnalytics,
) : ViewModel(), StateHolder<MonsterDetailViewState> {

    private val stateHolder = MonsterDetailStateHolder(
        getMonsterDetailUseCase,
        cloneMonster,
        changeMonstersMeasurementUnitUseCase,
        deleteMonsterUseCase,
        spellDetailEventDispatcher,
        monsterDetailEventListener,
        monsterDetailEventDispatcher,
        monsterLoreDetailEventDispatcher,
        folderInsertEventDispatcher,
        monsterRegistrationEventDispatcher,
        monsterRegistrationEventListener,
        dispatcher,
        initialState = savedStateHandle.getState().asMonsterDetailState(),
        monsterIndex = savedStateHandle["index"] ?: "",
        monsterIndexes = savedStateHandle["indexes"] ?: emptyList(),
        analytics = analytics,
    )

    private val _state = MutableStateFlow(savedStateHandle.getState())
    override val state: StateFlow<MonsterDetailViewState> = _state

    init {
        stateHolder.state
            .onEach {
                _state.value = it.also {
                    savedStateHandle["index"] = stateHolder.monsterIndex
                    savedStateHandle["indexes"] = stateHolder.monsterIndexes
                }.asMonsterDetailViewState().saveState(savedStateHandle)
            }
            .launchIn(viewModelScope)
    }

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        stateHolder.onMonsterChanged(monsterIndex, scrolled)
    }

    fun onShowOptionsClicked() = stateHolder.onShowOptionsClicked()

    fun onShowOptionsClosed() = stateHolder.onShowOptionsClosed()

    fun onOptionClicked(option: MonsterDetailOptionState) {
        stateHolder.onOptionClicked(
            br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.valueOf(option.name)
        )
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

    private fun MonsterDetailViewState.asMonsterDetailState(): MonsterDetailState {
        return MonsterDetailState(
            showDetail = showDetail,
        )
    }

    private fun MonsterDetailState.asMonsterDetailViewState(): MonsterDetailViewState {
        return MonsterDetailViewState(
            initialMonsterListPositionIndex = initialMonsterListPositionIndex,
            monsters = monsters.asState(),
            showOptions = showOptions,
            options = options.map { MonsterDetailOptionState.valueOf(it.name) },
            showDetail = showDetail,
            isLoading = isLoading,
            showCloneForm = showCloneForm,
            monsterCloneName = monsterCloneName,
        )
    }
}

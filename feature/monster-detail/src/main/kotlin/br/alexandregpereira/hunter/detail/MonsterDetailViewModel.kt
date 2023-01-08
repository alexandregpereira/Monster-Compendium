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

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.detail.MonsterDetailOptionState.ADD_TO_FOLDER
import br.alexandregpereira.hunter.detail.MonsterDetailOptionState.CHANGE_TO_FEET
import br.alexandregpereira.hunter.detail.MonsterDetailOptionState.CHANGE_TO_METERS
import br.alexandregpereira.hunter.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@HiltViewModel
internal class MonsterDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MonsterDetailViewState> = _state

    private var monsterIndex: String
        get() = savedStateHandle["index"] ?: ""
        set(value) {
            savedStateHandle["index"] = value
        }

    private var monsterIndexes: List<String>
        get() = savedStateHandle["indexes"] ?: emptyList()
        set(value) {
            savedStateHandle["indexes"] = value
        }

    init {
        observeEvents()
        state.value.run {
            if (showDetail && monsters.isEmpty()) {
                getMonstersByInitialIndex(monsterIndex, monsterIndexes)
            }
        }
    }

    private fun observeEvents() {
        monsterDetailEventListener.events.onEach { event ->
            when (event) {
                is Show -> {
                    getMonstersByInitialIndex(event.index, event.indexes)
                    setState { copy(showDetail = true) }
                }
                Hide -> setState { copy(showDetail = false) }
            }
        }.launchIn(viewModelScope)
    }

    private fun getMonstersByInitialIndex(monsterIndex: String, monsterIndexes: List<String>) {
        onMonsterChanged(monsterIndex)
        this.monsterIndexes = monsterIndexes
        setState { savedStateHandle.getState() }
        getMonsterDetail().collectDetail()
    }

    fun onMonsterChanged(monsterIndex: String) {
        this.monsterIndex = monsterIndex
    }

    fun onShowOptionsClicked() {
        setState { ShowOptions }
    }

    fun onShowOptionsClosed() {
        setState { HideOptions }
    }

    fun onOptionClicked(option: MonsterDetailOptionState) {
        setState { HideOptions }
        when (option) {
            ADD_TO_FOLDER -> folderInsertEventDispatcher.dispatchEvent(
                FolderInsertEvent.Show(monsterIndexes = listOf(monsterIndex))
            )
            CHANGE_TO_FEET -> {
                changeMeasurementUnit(MeasurementUnit.FEET)
            }
            CHANGE_TO_METERS -> {
                changeMeasurementUnit(MeasurementUnit.METER)
            }
        }
    }

    fun onSpellClicked(spellIndex: String) {
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    fun onLoreClicked(monsterIndex: String) {
        monsterLoreDetailEventDispatcher.dispatchEvent(MonsterLoreDetailEvent.Show(monsterIndex))
    }

    fun onClose() {
        monsterDetailEventDispatcher.dispatchEvent(Hide)
    }

    private fun getMonsterDetail(): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(monsterIndex, indexes = monsterIndexes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        changeMonstersMeasurementUnitUseCase(measurementUnit)
            .flatMapLatest { getMonsterDetail() }
            .collectDetail()
    }

    private fun Flow<MonsterDetail>.collectDetail() {
        this.map {
            Log.i("MonsterDetailViewModel", "collectDetail")
            val measurementUnit = it.measurementUnit
            getState().complete(
                initialMonsterIndex = it.monsterIndexSelected,
                monsters = it.monsters.asState(),
                options = when (measurementUnit) {
                    MeasurementUnit.FEET -> listOf(ADD_TO_FOLDER, CHANGE_TO_METERS)
                    MeasurementUnit.METER -> listOf(ADD_TO_FOLDER, CHANGE_TO_FEET)
                }
            )
        }.flowOn(dispatcher)
            .catch {
                Log.e("MonsterDetailViewModel", it.message ?: "")
                it.printStackTrace()
            }.onEach { state ->
                setState { state }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun getState(): MonsterDetailViewState = withContext(Dispatchers.Main) {
        state.value
    }

    private fun setState(block: MonsterDetailViewState.() -> MonsterDetailViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}

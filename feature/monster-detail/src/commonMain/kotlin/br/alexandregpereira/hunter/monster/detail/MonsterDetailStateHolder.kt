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

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnMonsterPageChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.CHANGE_TO_FEET
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.CHANGE_TO_METERS
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.DefaultMutableStateHolder
import br.alexandregpereira.hunter.state.MutableStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class MonsterDetailStateHolder(
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterDetailAnalytics,
    initialState: MonsterDetailState = MonsterDetailState(),
    monsterIndex: String = "",
    monsterIndexes: List<String> = emptyList()
) : ScopeManager(),
    MutableStateHolder<MonsterDetailState> by DefaultMutableStateHolder(initialState) {

    var monsterIndex: String = ""
        private set

    var monsterIndexes: List<String> = emptyList()
        private set

    private var currentJob: Job? = null
    private var enableMonsterPageChangesEventDispatch = false

    init {
        observeEvents()
        state.value.run {
            if (showDetail && monsters.isEmpty()) {
                getMonstersByInitialIndex(monsterIndex, monsterIndexes)
            }
        }
    }

    private fun observeEvents() {
        monsterDetailEventListener.collectOnVisibilityChanges { event ->
            when (event) {
                is Show -> {
                    analytics.trackMonsterDetailShown(event)
                    enableMonsterPageChangesEventDispatch = event.enableMonsterPageChangesEventDispatch
                    getMonstersByInitialIndex(event.index, event.indexes)
                    setState { copy(showDetail = true) }
                }
                Hide -> {
                    analytics.trackMonsterDetailHidden()
                    setState { copy(showDetail = false) }
                }
            }
        }.launchIn(scope)
    }

    private fun getMonstersByInitialIndex(monsterIndex: String, monsterIndexes: List<String>) {
        this.monsterIndexes = monsterIndexes
        onMonsterChanged(monsterIndex, scrolled = false)
        getMonsterDetail().collectDetail()
    }

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        if (enableMonsterPageChangesEventDispatch && scrolled && monsterIndex != this.monsterIndex) {
            analytics.trackMonsterPageChanged(monsterIndex, scrolled)
            monsterDetailEventDispatcher.dispatchEvent(OnMonsterPageChanges(monsterIndex))
        }
        this.monsterIndex = monsterIndex
    }

    fun onShowOptionsClicked() {
        analytics.trackMonsterDetailOptionsShown()
        setState { ShowOptions }
    }

    fun onShowOptionsClosed() {
        analytics.trackMonsterDetailOptionsHidden()
        setState { HideOptions }
    }

    fun onOptionClicked(option: MonsterDetailOptionState) {
        analytics.trackMonsterDetailOptionClicked(option)
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
        analytics.trackMonsterDetailSpellClicked(spellIndex)
        spellDetailEventDispatcher.dispatchEvent(SpellDetailEvent.ShowSpell(spellIndex))
    }

    fun onLoreClicked(monsterIndex: String) {
        analytics.trackMonsterDetailLoreClicked(monsterIndex)
        monsterLoreDetailEventDispatcher.dispatchEvent(MonsterLoreDetailEvent.Show(monsterIndex))
    }

    fun onClose() {
        analytics.trackMonsterDetailClosed()
        monsterDetailEventDispatcher.dispatchEvent(Hide)
    }

    private fun getMonsterDetail(): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(monsterIndex, indexes = monsterIndexes)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun changeMeasurementUnit(measurementUnit: MeasurementUnit) {
        changeMonstersMeasurementUnitUseCase(monsterIndex, measurementUnit)
            .flatMapLatest { getMonsterDetail() }
            .collectDetail()
    }

    private fun Flow<MonsterDetail>.collectDetail() {
        currentJob?.cancel()
        currentJob = this.cancellable()
            .map {
                val measurementUnit = it.measurementUnit
                MonsterDetailState(
                    initialMonsterListPositionIndex = it.monsterIndexSelected,
                    monsters = it.monsters,
                    options = when (measurementUnit) {
                        MeasurementUnit.FEET -> listOf(ADD_TO_FOLDER, CHANGE_TO_METERS)
                        MeasurementUnit.METER -> listOf(ADD_TO_FOLDER, CHANGE_TO_FEET)
                    }
                )
            }.flowOn(dispatcher)
            .onStart {
                setState { copy(isLoading = true) }
            }
            .catch {
                setState { copy(isLoading = false) }
                it.printStackTrace()
                analytics.logException(it)
            }.onEach { state ->
                analytics.trackMonsterDetailLoaded(monsterIndex, state.monsters)
                setState {
                    complete(
                        initialMonsterListPositionIndex = state.initialMonsterListPositionIndex,
                        monsters = state.monsters,
                        options = state.options
                    )
                }
            }
            .launchIn(scope)
    }
}

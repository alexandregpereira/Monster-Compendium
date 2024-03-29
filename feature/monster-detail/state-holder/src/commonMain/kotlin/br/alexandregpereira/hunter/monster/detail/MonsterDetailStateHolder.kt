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
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.usecase.ChangeMonstersMeasurementUnitUseCase
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnCompendiumChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnMonsterPageChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.AddToFolder
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.CHANGE_TO_FEET
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.CHANGE_TO_METERS
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.CLONE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.ChangeToFeet
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.ChangeToMeters
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Clone
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.DELETE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Delete
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.EDIT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Edit
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationResult
import br.alexandregpereira.hunter.monster.registration.event.collectOnSaved
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.native.ObjCName

@ObjCName(name = "MonsterDetailStateHolder", exact = true)
class MonsterDetailStateHolder(
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val cloneMonster: CloneMonsterUseCase,
    private val changeMonstersMeasurementUnitUseCase: ChangeMonstersMeasurementUnitUseCase,
    private val deleteMonster: DeleteMonsterUseCase,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val monsterRegistrationEventDispatcher: EventDispatcher<MonsterRegistrationEvent>,
    private val monsterRegistrationEventListener: EventListener<MonsterRegistrationResult>,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterDetailAnalytics,
    private val appLocalization: AppLocalization,
    private val stateRecovery: MonsterDetailStateRecovery,
) : UiModel<MonsterDetailState>(stateRecovery.state.copy(strings = appLocalization.getStrings())) {

    private val monsterIndex: String
        get() = stateRecovery.monsterIndex

    private val monsterIndexes: List<String>
        get() = stateRecovery.monsterIndexes

    private var currentJob: Job? = null
    private var enableMonsterPageChangesEventDispatch = false
    private var metadata: List<Monster> = emptyList()

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
                    enableMonsterPageChangesEventDispatch =
                        event.enableMonsterPageChangesEventDispatch
                    getMonstersByInitialIndex(event.index, event.indexes)
                    setState {
                        copy(
                            showDetail = true,
                            strings = appLocalization.getStrings()
                        ).saveState(stateRecovery)
                    }
                }

                Hide -> {
                    analytics.trackMonsterDetailHidden()
                    setState { copy(showDetail = false).saveState(stateRecovery) }
                }
            }
        }.launchIn(scope)

        monsterRegistrationEventListener.collectOnSaved {
            getMonstersByInitialIndex(monsterIndex, monsterIndexes, invalidateCache = true)
        }.launchIn(scope)
    }

    private fun getMonstersByInitialIndex(
        monsterIndex: String,
        monsterIndexes: List<String>,
        invalidateCache: Boolean = false
    ) {
        stateRecovery.saveMonsterIndexes(monsterIndexes)
        onMonsterChanged(monsterIndex, scrolled = false)
        getMonsterDetail(invalidateCache = invalidateCache).collectDetail()
    }

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        if (enableMonsterPageChangesEventDispatch && scrolled && monsterIndex != this.monsterIndex) {
            analytics.trackMonsterPageChanged(monsterIndex, scrolled)
            monsterDetailEventDispatcher.dispatchEvent(OnMonsterPageChanges(monsterIndex))
        }
        stateRecovery.saveMonsterIndex(monsterIndex)
        setState { changeOptions() }
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
        when (option.id) {
            ADD_TO_FOLDER -> folderInsertEventDispatcher.dispatchEvent(
                FolderInsertEvent.Show(monsterIndexes = listOf(monsterIndex))
            )

            CLONE -> showCloneForm()

            EDIT -> {
                analytics.trackMonsterDetailEditClicked(monsterIndex)
                setState { HideOptions }
                monsterRegistrationEventDispatcher.dispatchEvent(
                    MonsterRegistrationEvent.ShowEdit(monsterIndex)
                )
            }

            DELETE -> {
                analytics.trackMonsterDetailDeleteClicked(monsterIndex)
                setState { copy(showDeleteConfirmation = true) }
            }

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

    fun onCloneFormClosed() {
        setState { hideCloneForm() }
    }

    fun onCloneFormChanged(monsterName: String) {
        setState {
            copy(monsterCloneName = monsterName)
        }
    }

    fun onCloneFormSaved() {
        setState { hideCloneForm() }
        cloneMonster()
    }

    fun onDeleteConfirmed() {
        analytics.trackMonsterDetailDeleteConfirmed(monsterIndex)
        setState { copy(showDeleteConfirmation = false) }
        deleteMonster()
    }

    fun onDeleteClosed() {
        analytics.trackMonsterDetailDeleteCanceled(monsterIndex)
        setState { copy(showDeleteConfirmation = false) }
    }

    private fun getMonsterDetail(
        monsterIndex: String = this.monsterIndex,
        invalidateCache: Boolean = false
    ): Flow<MonsterDetail> {
        return getMonsterDetailUseCase(
            monsterIndex,
            indexes = monsterIndexes,
            invalidateCache = invalidateCache
        )
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
            .toMonsterDetailState()
            .flowOn(dispatcher)
            .onStart {
                setState { copy(isLoading = true) }
            }
            .catch {
                setState { copy(isLoading = false) }
                it.printStackTrace()
                analytics.logException(it)
            }
            .emitState()
            .onEach { state ->
                analytics.trackMonsterDetailLoaded(monsterIndex, state.monsters)
            }
            .launchIn(scope)
    }

    private fun Flow<MonsterDetail>.toMonsterDetailState(): Flow<MonsterDetailState> {
        return map {
            metadata = it.monsters
            val strings = appLocalization.getStrings()
            MonsterDetailState(
                initialMonsterListPositionIndex = it.monsterIndexSelected,
                monsters = it.monsters.asState(strings),
                measurementUnit = it.measurementUnit,
            ).changeOptions()
        }
    }

    private fun Flow<MonsterDetailState>.emitState(): Flow<MonsterDetailState> {
        return onEach { state ->
            setState {
                complete(
                    initialMonsterListPositionIndex = state.initialMonsterListPositionIndex,
                    monsters = state.monsters,
                    options = state.options
                ).saveState(stateRecovery)
            }
        }
    }

    private fun MonsterDetailState.changeOptions(): MonsterDetailState {
        val monster = metadata.find { monster -> monster.index == monsterIndex } ?: return this
        val editOption = if (monster.isClone) {
            listOf(Edit(strings), Delete(strings))
        } else emptyList()

        return copy(
            options = listOf(AddToFolder(strings), Clone(strings)) + editOption + when (measurementUnit) {
                MeasurementUnit.FEET -> ChangeToFeet(strings)
                MeasurementUnit.METER -> ChangeToMeters(strings)
            }
        )
    }

    private fun showCloneForm() = setState {
        this.showCloneForm(monsterIndex).saveState(stateRecovery)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun cloneMonster() {
        val monsterCloneName = state.value.monsterCloneName
        val currentState = state.value
        val monsterIndexes = monsterIndexes
        val currentMonsterIndex = monsterIndex
        setState { copy(isLoading = true) }
        cloneMonster(monsterIndex, monsterName = monsterCloneName).flatMapConcat { monsterIndex ->
            if (monsterIndexes.isEmpty()) {
                getMonsterDetail(monsterIndex, invalidateCache = true)
                    .toMonsterDetailState()
                    .map { state ->
                        state to monsterIndex
                    }
            } else flowOf(currentState to currentMonsterIndex)
        }.flowOn(dispatcher)
            .map { (state, monsterIndex) ->
                onMonsterChanged(monsterIndex, scrolled = true)
                if (monsterIndexes.isNotEmpty()) {
                    monsterDetailEventDispatcher.dispatchEvent(OnCompendiumChanges)
                }
                state
            }
            .emitState()
            .launchIn(scope)
    }

    private fun deleteMonster() {
        deleteMonster(monsterIndex)
            .flowOn(dispatcher)
            .onEach {
                monsterDetailEventDispatcher.dispatchEvent(OnCompendiumChanges)
                monsterDetailEventDispatcher.dispatchEvent(Hide)
            }
            .launchIn(scope)
    }
}

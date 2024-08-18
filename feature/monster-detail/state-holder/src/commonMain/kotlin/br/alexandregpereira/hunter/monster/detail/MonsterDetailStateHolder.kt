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

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEvent
import br.alexandregpereira.hunter.event.monster.lore.detail.MonsterLoreDetailEventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.AddToFolder
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Clone
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Delete
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Edit
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.Export
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionState.Companion.ResetToOriginal
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.ADD_TO_FOLDER
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.CLONE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.DELETE
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EDIT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.EXPORT
import br.alexandregpereira.hunter.monster.detail.MonsterDetailOptionStateId.RESET_TO_ORIGINAL
import br.alexandregpereira.hunter.monster.detail.domain.CloneMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.DeleteMonsterUseCase
import br.alexandregpereira.hunter.monster.detail.domain.GetMonsterDetailUseCase
import br.alexandregpereira.hunter.monster.detail.domain.ResetMonsterToOriginal
import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnCompendiumChanges
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnMonsterPageChanges
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.monster.event.collectOnVisibilityChanges
import br.alexandregpereira.hunter.monster.registration.event.MonsterRegistrationEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEvent
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailEventDispatcher
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.withContext

class MonsterDetailStateHolder internal constructor(
    private val getMonsterDetailUseCase: GetMonsterDetailUseCase,
    private val cloneMonster: CloneMonsterUseCase,
    private val deleteMonster: DeleteMonsterUseCase,
    private val resetMonsterToOriginal: ResetMonsterToOriginal,
    private val spellDetailEventDispatcher: SpellDetailEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val shareContentEventDispatcher: ShareContentEventDispatcher,
    private val monsterLoreDetailEventDispatcher: MonsterLoreDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val monsterRegistrationEventDispatcher: EventDispatcher<MonsterRegistrationEvent>,
    private val syncEventDispatcher: SyncEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: MonsterDetailAnalytics,
    private val appLocalization: AppLocalization,
    private val stateRecovery: StateRecovery,
) : UiModel<MonsterDetailState>(MonsterDetailState(strings = appLocalization.getStrings())) {

    private val monsterIndex: String
        get() = stateRecovery.monsterIndex

    private val monsterIndexes: List<String>
        get() = stateRecovery.monsterIndexes

    private var currentJob: Job? = null
    private var enableMonsterPageChangesEventDispatch = false
    private var metadata: List<Monster> = emptyList()
    var initialMonsterListPositionIndex: Int = 0
        private set

    init {
        setState { updateState(stateRecovery) }
        observeEvents()
        state.value.run {
            if (showDetail && monsters.isEmpty()) {
                getMonstersByInitialIndex(monsterIndex, monsterIndexes)
            }
        }
    }

    private fun observeEvents() {
        monsterEventDispatcher.collectOnVisibilityChanges { event ->
            when (event) {
                is Show -> {
                    analytics.trackMonsterDetailShown(event)
                    enableMonsterPageChangesEventDispatch =
                        event.enableMonsterPageChangesEventDispatch
                    setState {
                        copy(
                            showDetail = true,
                            strings = appLocalization.getStrings()
                        ).saveState(stateRecovery)
                    }
                    getMonstersByInitialIndex(event.index, event.indexes)
                }

                Hide -> {
                    analytics.trackMonsterDetailHidden()
                    setState { copy(showDetail = false).saveState(stateRecovery) }
                }
            }
        }.launchIn(scope)

        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            getMonstersByInitialIndex(monsterIndex, monsterIndexes, invalidateCache = true)
        }.launchIn(scope)
    }

    private fun getMonstersByInitialIndex(
        monsterIndex: String,
        monsterIndexes: List<String>,
        invalidateCache: Boolean = false,
        loadingSubtle: Boolean = false,
    ) {
        stateRecovery.saveMonsterIndexes(monsterIndexes)
        onMonsterChanged(monsterIndex, scrolled = false)
        currentJob?.cancel()
        currentJob = getMonsterDetail(invalidateCache = invalidateCache)
            .cancellable()
            .toMonsterDetailState()
            .flowOn(dispatcher)
            .onStart {
                if (loadingSubtle.not()) {
                    setState { copy(isLoading = true) }
                }
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

    fun onMonsterChanged(monsterIndex: String, scrolled: Boolean = true) {
        if (scrolled && monsterIndex != this.monsterIndex) {
            val monsterPositionIndex = state.value.monsters.indexOfFirst { it.index == monsterIndex }
            initialMonsterListPositionIndex = monsterPositionIndex.takeIf { it >= 0 }
                ?: initialMonsterListPositionIndex
            if (enableMonsterPageChangesEventDispatch) {
                analytics.trackMonsterPageChanged(monsterIndex, scrolled)
                monsterEventDispatcher.dispatchEvent(OnMonsterPageChanges(monsterIndex))
            }

            state.value.monsters.getOrNull(monsterPositionIndex)?.let { monster ->
                if (monster.isComplete().not()) {
                    getMonstersByInitialIndex(monsterIndex, monsterIndexes, loadingSubtle = true)
                }
            }
        }
        stateRecovery.saveMonsterIndex(monsterIndex)
    }

    fun onShowOptionsClicked() {
        analytics.trackMonsterDetailOptionsShown()
        setState { ShowOptions.changeOptions() }
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

            RESET_TO_ORIGINAL -> {
                analytics.trackMonsterDetailResetToOriginalClicked(monsterIndex)
                setState { copy(showResetConfirmation = true) }
            }

            EXPORT -> {
                analytics.trackMonsterDetailExportClicked(monsterIndex)
                shareContentEventDispatcher.dispatchEvent(
                    ShareContentEvent.Export.OnStart(listOf(monsterIndex))
                )
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
        monsterEventDispatcher.dispatchEvent(Hide)
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

    fun onResetConfirmed() {
        analytics.trackMonsterDetailResetConfirmed(monsterIndex)
        setState { copy(showResetConfirmation = false) }
        resetMonster()
    }

    fun onResetClosed() {
        setState { copy(showResetConfirmation = false) }
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

    private fun Flow<MonsterDetail>.toMonsterDetailState(): Flow<MonsterDetailState> {
        return map {
            val strings = appLocalization.getStrings()
            withContext(Dispatchers.Main) {
                metadata = it.monsters
                initialMonsterListPositionIndex = it.monsterIndexSelected
            }
            MonsterDetailState(
                monsters = it.monsters.asState(strings),
                measurementUnit = it.measurementUnit,
                strings = strings,
            ).changeOptions()
        }
    }

    private fun Flow<MonsterDetailState>.emitState(): Flow<MonsterDetailState> {
        return onEach { state ->
            setState {
                complete(
                    monsters = state.monsters,
                    options = state.options
                ).saveState(stateRecovery)
            }
        }
    }

    private fun MonsterDetailState.changeOptions(): MonsterDetailState {
        val monster = metadata.find { monster -> monster.index == monsterIndex } ?: return this
        val editOption = when (monster.status) {
            MonsterStatus.Original -> listOf(Edit(strings))
            MonsterStatus.Imported,
            MonsterStatus.Clone -> listOf(Edit(strings), Delete(strings))
            MonsterStatus.Edited -> listOf(Edit(strings), ResetToOriginal(strings))
        }

        return copy(
            options = listOf(AddToFolder(strings), Export(strings), Clone(strings)) + editOption
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
            .flatMapLatest { (state, monsterIndex) ->
                flowOf(state).emitState().map { monsterIndex }
            }
            .onEach { monsterIndex ->
                onMonsterChanged(monsterIndex, scrolled = true)
                if (monsterIndexes.isNotEmpty()) {
                    monsterEventDispatcher.dispatchEvent(OnCompendiumChanges())
                }
            }
            .launchIn(scope)
    }

    private fun deleteMonster() {
        deleteMonster(monsterIndex)
            .onEach {
                monsterEventDispatcher.dispatchEvent(OnCompendiumChanges())
                monsterEventDispatcher.dispatchEvent(Hide)
            }
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    private fun resetMonster() {
        resetMonsterToOriginal(monsterIndex)
            .flowOn(dispatcher)
            .onEach {
                syncEventDispatcher.startSync()
            }
            .launchIn(scope)
    }
}

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

package br.alexandregpereira.hunter.folder.preview

import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent.*
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnMonsterRemoved
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.ClearFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.HideFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResult.OnFolderPreviewPreviewVisibilityChanges
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FolderPreviewStateHolder internal constructor(
    private val stateRecovery: StateRecovery,
    private val folderPreviewEventManager: FolderPreviewEventManager,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase,
    private val addMonsterToFolderPreview: AddMonsterToFolderPreviewUseCase,
    private val removeMonsterFromFolderPreview: RemoveMonsterFromFolderPreviewUseCase,
    private val clearFolderPreviewUseCase: ClearFolderPreviewUseCase,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: FolderPreviewAnalytics,
    private val appLocalization: AppLocalization,
) : UiModel<FolderPreviewState>(stateRecovery.getState()) {

    init {
        observeEvents()
        if (state.value.showPreview && state.value.monsters.isEmpty()) {
            loadMonsters()
        }
    }

    fun onItemClick(monsterIndex: String) {
        analytics.trackItemClick(monsterIndex)
        navigateToMonsterDetail(monsterIndex)
    }

    fun onItemLongClick(monsterIndex: String) {
        analytics.trackItemLongClick(monsterIndex)
        removeMonster(monsterIndex)
    }

    fun onSave() {
        analytics.trackSave()
        folderInsertEventDispatcher.dispatchEvent(
            event = Show(monsterIndexes = state.value.monsters.map { it.index })
        ).onEach { result ->
            when (result) {
                is FolderInsertResult.OnSaved -> {
                    analytics.trackSaveSuccess()
                    clear()
                }
                is OnMonsterRemoved -> {
                    analytics.trackSaveMonsterRemoved()
                    removeMonster(result.monsterIndex)
                }
            }
        }.launchIn(scope)
    }

    private fun observeEvents() {
        scope.launch {
            folderPreviewEventManager.events.collect { event ->
                when (event) {
                    is AddMonster -> {
                        analytics.trackAddMonster(event.index)
                        addMonster(event.index)
                    }
                    is HideFolderPreview -> {
                        hideFolderPreview()
                    }
                    is ShowFolderPreview -> {
                        loadMonsters()
                    }
                }
            }
        }
    }

    private fun clear() {
        hideFolderPreview()
        clearFolderPreviewUseCase()
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    private fun addMonster(index: String) {
        addMonsterToFolderPreview(index)
            .flowOn(dispatcher)
            .onEach { monsters ->
                setState { changeMonsters(monsters) }
            }
            .launchIn(scope)
    }

    private fun loadMonsters() {
        getMonstersFromFolderPreview()
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { monsters ->
                val showPreview = monsters.isNotEmpty()
                setState {
                    changeMonsters(monsters = monsters)
                        .changeShowPreview(showPreview).also {
                            if (it.monsters.isNotEmpty() || it.showPreview) {
                                analytics.trackLoadMonstersResult(it)
                            }
                        }.copy(strings = appLocalization.getStrings())
                        .saveState(stateRecovery)
                }
                dispatchFolderPreviewVisibilityChangesEvent()
            }
            .launchIn(scope)
    }

    private fun removeMonster(monsterIndex: String) {
        removeMonsterFromFolderPreview(monsterIndex)
            .flowOn(dispatcher)
            .onEach { monsters ->
                val showPreview = monsters.isNotEmpty()
                setState {
                    changeMonsters(monsters = monsters).changeShowPreview(showPreview)
                        .saveState(stateRecovery)
                }
                if (showPreview.not()) {
                    dispatchFolderPreviewVisibilityChangesEvent()
                }
            }
            .launchIn(scope)
    }

    private fun FolderPreviewState.changeShowPreview(
        show: Boolean,
    ): FolderPreviewState {
        if (show != showPreview) {
            if (show) analytics.trackShowFolderPreview()
            else analytics.trackHideFolderPreview()
        }
        return this.copy(showPreview = show)
    }

    private fun hideFolderPreview() {
        setState { changeShowPreview(show = false).saveState(stateRecovery) }
        dispatchFolderPreviewVisibilityChangesEvent()
    }

    private fun dispatchFolderPreviewVisibilityChangesEvent() {
        folderPreviewEventManager.dispatchResult(
            OnFolderPreviewPreviewVisibilityChanges(isShowing = state.value.showPreview)
        )
    }

    private fun navigateToMonsterDetail(monsterIndex: String) {
        monsterDetailEventDispatcher.dispatchEvent(
            Show(index = monsterIndex, indexes = state.value.monsters.map { it.index })
        )
    }
}

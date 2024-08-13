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
import br.alexandregpereira.hunter.folder.preview.domain.AddMonsterToFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.ClearFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.GetMonstersFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.domain.RemoveMonsterFromFolderPreviewUseCase
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FolderPreviewStateHolder internal constructor(
    private val folderPreviewEventManager: FolderPreviewEventManager,
    private val getMonstersFromFolderPreview: GetMonstersFromFolderPreviewUseCase,
    private val addMonsterToFolderPreview: AddMonsterToFolderPreviewUseCase,
    private val removeMonsterFromFolderPreview: RemoveMonsterFromFolderPreviewUseCase,
    private val clearFolderPreviewUseCase: ClearFolderPreviewUseCase,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val folderInsertEventDispatcher: FolderInsertEventDispatcher,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: FolderPreviewAnalytics,
    private val appLocalization: AppLocalization,
) : UiModel<FolderPreviewState>(FolderPreviewState()) {

    init {
        observeEvents()
        loadMonsters()
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
                }
            }
        }
        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            loadMonsters()
        }.launchIn(scope)
    }

    private fun clear() {
        clearFolderPreviewUseCase()
            .flowOn(dispatcher)
            .onEach {
                loadMonsters()
            }
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
            .map { monsters ->
                setState {
                    changeMonsters(monsters = monsters)
                        .copy(strings = appLocalization.getStrings())
                }
            }
            .launchIn(scope)
    }

    private fun removeMonster(monsterIndex: String) {
        removeMonsterFromFolderPreview(monsterIndex)
            .flowOn(dispatcher)
            .onEach { monsters ->
                setState {
                    changeMonsters(monsters = monsters).trackShowPreview()
                }
            }
            .launchIn(scope)
    }

    private fun FolderPreviewState.trackShowPreview(): FolderPreviewState {
        if (showPreview) analytics.trackShowFolderPreview()
        else analytics.trackHideFolderPreview()
        return this
    }

    private fun navigateToMonsterDetail(monsterIndex: String) {
        monsterEventDispatcher.dispatchEvent(
            Show(index = monsterIndex, indexes = state.value.monsters.map { it.index })
        )
    }
}

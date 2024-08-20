/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.state.MutableActionHandler
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
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
) : UiModel<FolderPreviewState>(FolderPreviewState()),
    MutableActionHandler<FolderPreviewAction> by MutableActionHandler() {

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

    fun onClear() {
        analytics.trackClear()
        scope.launch {
            setState { copy(showPreview = false) }
            delay(300)
            clear()
        }
    }

    private fun observeEvents() {
        scope.launch {
            folderPreviewEventManager.events.collect { event ->
                when (event) {
                    is AddMonster -> {
                        analytics.trackAddMonster(event.indexes)
                        addMonster(event.indexes)
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

    private fun addMonster(indexes: List<String>) {
        addMonsterToFolderPreview(indexes)
            .flowOn(dispatcher)
            .onEach { monsters ->
                val previousMonsterListSize = state.value.monsters.size
                setState { changeMonsters(monsters) }
                if (monsters.size > previousMonsterListSize) {
                    delay(300)
                    sendAction(FolderPreviewAction.ScrollToStart)
                }
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

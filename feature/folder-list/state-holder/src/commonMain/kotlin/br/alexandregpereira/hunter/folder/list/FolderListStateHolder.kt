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

package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonstersByFolders
import br.alexandregpereira.hunter.domain.folder.RemoveMonsterFoldersUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent.Show
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.event.folder.list.FolderListResult.OnItemSelectionVisibilityChanges
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.ui.StateRecovery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class FolderListStateHolder internal constructor(
    private val getMonsterFolders: GetMonsterFoldersUseCase,
    private val removeMonsterFolders: RemoveMonsterFoldersUseCase,
    private val folderInsertResultListener: FolderInsertResultListener,
    private val folderDetailEventDispatcher: FolderDetailEventDispatcher,
    private val folderListEventManager: FolderListEventManager,
    private val dispatcher: CoroutineDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val analytics: FolderListAnalytics,
    private val appLocalization: AppLocalization,
    private val stateRecovery: StateRecovery,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val getMonstersByFolders: GetMonstersByFolders,
) : UiModel<FolderListState>(stateRecovery.getState()) {

    private val strings: FolderListStrings
        get() = getFolderListStrings(appLocalization.getLanguage())

    init {
        observeFolderInsertResults()
        observeEvents()
        loadMonsterFolders()
    }

    fun onItemClick(folderName: String) {
        if (state.value.itemSelectionEnabled) {
            onItemSelect(folderName)
            return
        }
        analytics.trackFolderClick(folderName)
        folderDetailEventDispatcher.dispatchEvent(Show(folderName = folderName))
    }

    fun onItemSelectionClose() {
        analytics.trackItemSelectionClose()
        setState {
            copy(
                folders = folders.map {
                    it.copy(selected = false)
                },
                isItemSelectionOpen = false,
            ).saveState(stateRecovery)
        }
        folderListEventManager.dispatchResult(OnItemSelectionVisibilityChanges(isShowing = false))
    }

    fun onItemSelectionDeleteClick() {
        analytics.trackItemSelectionDeleteClick()
        flowOf(state.value.itemSelection)
            .map { it.toList() }
            .map { folderNames ->
                removeMonsterFolders(folderNames).collect()
            }
            .flowOn(dispatcher)
            .onEach {
                onItemSelectionClose()
                loadMonsterFolders()
            }
            .launchIn(scope)
    }

    fun onItemSelectionAddToPreviewClick() {
        analytics.trackItemSelectionAddToPreviewClick()
        val folders = state.value.itemSelection
        getMonstersByFolders(folders.toList())
            .map { monster -> monster.map { it.index } }
            .flowOn(dispatcher)
            .onEach { monsterIndexes ->
                onItemSelectionClose()
                folderPreviewEventDispatcher.dispatchEvent(
                    FolderPreviewEvent.AddMonster(monsterIndexes)
                )
            }
            .launchIn(scope)
    }

    fun onScrollChanges(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        setState {
            copy(
                firstVisibleItemIndex = firstVisibleItemIndex,
                firstVisibleItemScrollOffset = firstVisibleItemScrollOffset,
            )
        }
    }

    fun onItemSelect(folderName: String) {
        analytics.trackItemSelect(folderName)
        setState {
            val newItemSelection = itemSelection.toMutableSet().also {
                val added = it.add(folderName)
                if (added.not()) {
                    it.remove(folderName)
                }
            }
            val isItemSelectionOpen = newItemSelection.isNotEmpty()
            val itemSelectionCount = if (isItemSelectionOpen) newItemSelection.size else itemSelectionCount
            copy(
                folders = folders.map {
                    it.copy(selected = newItemSelection.contains(it.folderName))
                },
                isItemSelectionOpen = isItemSelectionOpen,
                itemSelectionCount = itemSelectionCount,
            ).saveState(stateRecovery)
        }
        folderListEventManager.dispatchResult(
            OnItemSelectionVisibilityChanges(isShowing = state.value.isItemSelectionOpen)
        )
    }

    private fun loadMonsterFolders() {
        val selectedFolders = state.value.itemSelection
        getMonsterFolders()
            .map { it.map { folder -> folder to selectedFolders.contains(folder.name) } }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { folders ->
                analytics.trackFoldersLoaded(folders)
                setState {
                    copy(
                        folders = folders.asState(),
                        strings = this@FolderListStateHolder.strings
                    ).saveState(stateRecovery)
                }
            }
            .launchIn(scope)
    }

    private fun observeFolderInsertResults() {
        folderInsertResultListener.result
            .filter { result ->
                result is OnSaved
            }
            .onEach {
                loadMonsterFolders()
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            loadMonsterFolders()
        }.launchIn(scope)
    }
}

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

package br.alexandregpereira.hunter.folder.list

import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.RemoveMonsterFoldersUseCase
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEvent.Show
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailEventDispatcher
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResultListener
import br.alexandregpereira.hunter.event.folder.list.FolderListResult.OnItemSelectionVisibilityChanges
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.sync.event.collectSyncFinishedEvents
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
    private val syncEventListener: SyncEventListener,
    private val analytics: FolderListAnalytics,
    private val appLocalization: AppLocalization,
) : UiModel<FolderListState>(FolderListState()) {

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
            )
        }
        folderListEventManager.dispatchResult(OnItemSelectionVisibilityChanges(isShowing = false))
    }

    fun onItemSelectionDeleteClick() {
        analytics.trackItemSelectionDeleteClick()
        onItemSelectionClose()
        flowOf(state.value.itemSelection)
            .map { it.toList() }
            .map { folderNames ->
                removeMonsterFolders(folderNames).collect()
            }
            .flowOn(dispatcher)
            .onEach { loadMonsterFolders() }
            .launchIn(scope)
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
            )
        }
        folderListEventManager.dispatchResult(
            OnItemSelectionVisibilityChanges(isShowing = state.value.isItemSelectionOpen)
        )
    }

    private fun loadMonsterFolders() {
        getMonsterFolders()
            .map { it.map { folder -> folder to false } }
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
                    )
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
        syncEventListener.collectSyncFinishedEvents {
            loadMonsterFolders()
        }.launchIn(scope)
    }
}

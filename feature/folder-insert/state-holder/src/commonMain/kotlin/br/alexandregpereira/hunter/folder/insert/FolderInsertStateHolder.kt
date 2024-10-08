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

package br.alexandregpereira.hunter.folder.insert

import br.alexadregpereira.hunter.shareContent.event.ShareContentEvent
import br.alexadregpereira.hunter.shareContent.event.ShareContentEventDispatcher
import br.alexandregpereira.hunter.domain.folder.AddMonstersToFolderUseCase
import br.alexandregpereira.hunter.domain.folder.GetFolderMonsterPreviewsByIdsUseCase
import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertEvent.Show
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnMonsterRemoved
import br.alexandregpereira.hunter.event.folder.insert.FolderInsertResult.OnSaved
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class FolderInsertStateHolder internal constructor(
    private val getMonsterFolders: GetMonsterFoldersUseCase,
    private val getFolderMonsterPreviewsByIds: GetFolderMonsterPreviewsByIdsUseCase,
    private val addMonstersToFolder: AddMonstersToFolderUseCase,
    private val folderInsertEventManager: FolderInsertEventManager,
    private val dispatcher: CoroutineDispatcher,
    private val analytics: FolderInsertAnalytics,
    private val appLocalization: AppLocalization,
    private val shareContentEventDispatcher: ShareContentEventDispatcher,
) : UiModel<FolderInsertState>(FolderInsertState()) {

    private val strings: FolderInsertStrings
        get() = getFolderInsertStrings(appLocalization.getLanguage())

    init {
        observeEvents()
        if (state.value.isOpen && state.value.monsterPreviews.isEmpty()) {
            load(state.value.monsterIndexes, folderName = state.value.folderName)
        }
    }

    fun onFolderNameFieldChange(folderName: String) {
        setState {
            copy(
                folderName = folderName,
                folderIndexSelected = folders.map { it.first }
                    .indexOfFirst { it.equals(folderName, ignoreCase = true) }
            )
        }
    }

    fun onRemoveMonster(monsterIndex: String) {
        analytics.trackMonsterRemoved()
        loadMonsterPreviews(
            monsterIndexes = state.value.monsterIndexes.toMutableList().also {
                it.remove(monsterIndex)
            }
        )
        folderInsertEventManager.dispatchResult(OnMonsterRemoved(monsterIndex))
    }

    fun onSave() {
        if (state.value.folderName.isBlank()) return
        analytics.trackFolderSaved()

        onClose()
        val indexes = state.value.monsterIndexes
        addMonstersToFolder(
            folderName = state.value.folderName.trim(),
            indexes = indexes
        ).flowOn(dispatcher)
            .onCompletion {
                folderInsertEventManager.dispatchResult(OnSaved(indexes))
            }
            .launchIn(scope)
    }

    fun onClose() {
        analytics.trackClosed()
        setState { copy(isOpen = false) }
    }

    fun onShare() {
        analytics.trackShared()
        onClose()
        shareContentEventDispatcher.dispatchEvent(
            ShareContentEvent.Export.OnStart(state.value.monsterIndexes)
        )
    }

    private fun load(monsterIndexes: List<String>, folderName: String = "") {
        setState {
            copy(
                monsterIndexes = monsterIndexes,
                folderName = folderName,
                strings = this@FolderInsertStateHolder.strings,
            )
        }

        loadMonsterFolders()
        loadMonsterPreviews(monsterIndexes)
    }

    private fun loadMonsterFolders() {
        getMonsterFolders()
            .map { folders ->
                folders.map { it.name to it.monsters.size }
            }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { folders ->
                analytics.trackFoldersLoaded(folders)
                setState { copy(folders = folders) }
            }
            .launchIn(scope)
    }

    private fun loadMonsterPreviews(monsterIndexes: List<String>) {
        getFolderMonsterPreviewsByIds(monsterIndexes)
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .onEach { monsters ->
                analytics.trackMonsterPreviewsLoaded(monsters)
                setState {
                    copy(
                        monsterPreviews = monsters.asState(),
                        monsterIndexes = monsterIndexes,
                        isOpen = monsters.isNotEmpty()
                    )
                }
            }
            .launchIn(scope)
    }

    private fun observeEvents() {
        folderInsertEventManager.events.onEach { event ->
            when (event) {
                is Show -> {
                    analytics.trackOpened(event.monsterIndexes)
                    load(event.monsterIndexes)
                }
            }
        }.launchIn(scope)
    }
}

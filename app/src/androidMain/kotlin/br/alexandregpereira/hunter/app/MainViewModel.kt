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

package br.alexandregpereira.hunter.app

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.app.BottomBarItemIcon.COMPENDIUM
import br.alexandregpereira.hunter.app.BottomBarItemIcon.FOLDERS
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SEARCH
import br.alexandregpereira.hunter.app.BottomBarItemIcon.SETTINGS
import br.alexandregpereira.hunter.app.MainViewEvent.BottomNavigationItemClick
import br.alexandregpereira.hunter.event.folder.detail.FolderDetailResultListener
import br.alexandregpereira.hunter.event.folder.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.folder.list.FolderListResultListener
import br.alexandregpereira.hunter.event.folder.list.collectOnItemSelectionVisibilityChanges
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Hide
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.event.monster.detail.collectOnVisibilityChanges
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent.AddTopContent
import br.alexandregpereira.hunter.event.systembar.BottomBarEvent.RemoveTopContent
import br.alexandregpereira.hunter.event.systembar.BottomBarEventManager
import br.alexandregpereira.hunter.event.systembar.dispatchAddTopContentEvent
import br.alexandregpereira.hunter.event.systembar.dispatchRemoveTopContentEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.monster.content.event.MonsterContentManagerEventListener
import br.alexandregpereira.hunter.monster.content.event.collectOnVisibilityChanges
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val monsterDetailEventListener: MonsterDetailEventListener,
    private val folderDetailResultListener: FolderDetailResultListener,
    private val folderListResultListener: FolderListResultListener,
    private val monsterContentManagerEventListener: MonsterContentManagerEventListener,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val bottomBarEventManager: BottomBarEventManager,
    private val appLocalization: AppReactiveLocalization,
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle.getState())
    val state: StateFlow<MainViewState> = _state

    init {
        observeBottomBarEvents()
        observeMonsterDetailEvents()
        observeFolderDetailResults()
        observeFolderListResults()
        observeMonsterContentManagerEvents()
        observeLanguageChanges()
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach { language ->
            setState {
                val strings = language.getStrings()
                copy(
                    bottomBarItems = BottomBarItemIcon.entries.map {
                        when (it) {
                            COMPENDIUM -> BottomBarItem(icon = it, text = strings.compendium)
                            SEARCH -> BottomBarItem(icon = it, text = strings.search)
                            FOLDERS -> BottomBarItem(icon = it, text = strings.folders)
                            SETTINGS -> BottomBarItem(icon = it, text = strings.menu)
                        }
                    },
                    showBottomBar = topContentStack.isEmpty()
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeBottomBarEvents() {
        bottomBarEventManager.events.onEach { event ->
            when (event) {
                is AddTopContent -> setState { addTopContentStack(event.topContentId) }
                is RemoveTopContent -> setState { removeTopContentStack(event.topContentId) }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeMonsterDetailEvents() {
        monsterDetailEventListener.collectOnVisibilityChanges { event ->
            when (event) {
                is Show -> {
                    dispatchAddTopContentEvent(TopContent.MONSTER_DETAIL.name)
                    dispatchFolderPreviewEvent(show = false)
                }
                Hide -> {
                    dispatchRemoveTopContentEvent(TopContent.MONSTER_DETAIL.name)
                    dispatchFolderPreviewEvent(show = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun observeMonsterContentManagerEvents() {
        monsterContentManagerEventListener.collectOnVisibilityChanges { isShowing ->
            dispatchTopContentEvent(TopContent.MONSTER_CONTENT_MANAGER.name, isShowing)
        }.launchIn(viewModelScope)
    }

    private fun observeFolderDetailResults() {
        folderDetailResultListener.collectOnVisibilityChanges { result ->
            dispatchTopContentEvent(TopContent.FOLDER_DETAIL.name, result.isShowing)
        }.launchIn(viewModelScope)
    }

    private fun observeFolderListResults() {
        folderListResultListener.collectOnItemSelectionVisibilityChanges { result ->
            dispatchTopContentEvent(TopContent.FOLDER_LIST_SELECTION.name, result.isShowing)
            dispatchFolderPreviewEvent(result.isShowing.not())
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainViewEvent) {
        when (event) {
            is BottomNavigationItemClick -> {
                setState { copy(bottomBarItemSelectedIndex = bottomBarItems.indexOf(event.item)) }
                dispatchFolderPreviewEvent(show = event.item.icon != SETTINGS)
            }
        }
    }

    private fun dispatchFolderPreviewEvent(show: Boolean) {
        val folderPreviewEvent = if (show) {
            FolderPreviewEvent.ShowFolderPreview
        } else {
            FolderPreviewEvent.HideFolderPreview
        }
        folderPreviewEventDispatcher.dispatchEvent(folderPreviewEvent)
    }

    private fun dispatchAddTopContentEvent(topContentId: String) {
        bottomBarEventManager.dispatchAddTopContentEvent(topContentId)
    }

    private fun dispatchRemoveTopContentEvent(topContentId: String) {
        bottomBarEventManager.dispatchRemoveTopContentEvent(topContentId)
    }

    private fun dispatchTopContentEvent(topContentId: String, show: Boolean) {
        if (show) {
            dispatchAddTopContentEvent(topContentId)
        } else {
            dispatchRemoveTopContentEvent(topContentId)
        }
    }

    private fun setState(block: MainViewState.() -> MainViewState) {
        _state.value = state.value.block().saveState(savedStateHandle)
    }
}

private enum class TopContent {
    MONSTER_DETAIL,
    FOLDER_DETAIL,
    MONSTER_CONTENT_MANAGER,
    FOLDER_LIST_SELECTION,
}

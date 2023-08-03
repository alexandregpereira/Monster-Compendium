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

package br.alexandregpereira.hunter.monster.compendium

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.domain.usecase.GetLastCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.domain.usecase.SaveCompendiumScrollItemPositionUseCase
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventListener
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewResultListener
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewAction.GoToCompendiumIndex
import br.alexandregpereira.hunter.monster.compendium.domain.GetMonsterCompendiumUseCase
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAction
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumAnalytics
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumState
import br.alexandregpereira.hunter.monster.compendium.state.MonsterCompendiumStateHolder
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumErrorState.NO_INTERNET_CONNECTION
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCompendiumEvents
import br.alexandregpereira.hunter.sync.event.SyncEventDispatcher
import br.alexandregpereira.hunter.sync.event.SyncEventListener
import br.alexandregpereira.hunter.ui.compose.LoadingScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class MonsterCompendiumViewModel(
    private val savedStateHandle: SavedStateHandle,
    getMonsterCompendiumUseCase: GetMonsterCompendiumUseCase,
    getLastCompendiumScrollItemPositionUseCase: GetLastCompendiumScrollItemPositionUseCase,
    saveCompendiumScrollItemPositionUseCase: SaveCompendiumScrollItemPositionUseCase,
    folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    folderPreviewResultListener: FolderPreviewResultListener,
    monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    monsterDetailEventListener: MonsterDetailEventListener,
    syncEventListener: SyncEventListener,
    syncEventDispatcher: SyncEventDispatcher,
    analytics: MonsterCompendiumAnalytics,
    dispatcher: CoroutineDispatcher,
    loadOnInit: Boolean = true,
) : ViewModel(), MonsterCompendiumEvents {

    private val stateHolder: MonsterCompendiumStateHolder = MonsterCompendiumStateHolder(
        getMonsterCompendiumUseCase,
        getLastCompendiumScrollItemPositionUseCase,
        saveCompendiumScrollItemPositionUseCase,
        folderPreviewEventDispatcher,
        folderPreviewResultListener,
        monsterDetailEventDispatcher,
        monsterDetailEventListener,
        syncEventListener,
        syncEventDispatcher,
        dispatcher,
        loadOnInit = loadOnInit,
        initialState = savedStateHandle.getState().asMonsterCompendiumState(),
        analytics = analytics,
    )

    val initialScrollItemPosition: Int
        get() = stateHolder.initialScrollItemPosition

    private val _state = MutableStateFlow(stateHolder.state.value.asMonsterCompendiumViewState())
    val state: StateFlow<MonsterCompendiumViewState> = _state

    val action: Flow<MonsterCompendiumViewAction> = stateHolder.action.map {
        when (it) {
            is MonsterCompendiumAction.GoToCompendiumIndex -> GoToCompendiumIndex(it.index)
        }
    }

    init {
        stateHolder.state
            .onEach {
                _state.value = it.asMonsterCompendiumViewState().saveState(savedStateHandle)
            }
            .launchIn(viewModelScope)
    }

    fun loadMonsters() = stateHolder.loadMonsters()

    override fun onItemCLick(index: String) {
        stateHolder.onItemClick(index)
    }

    override fun onItemLongCLick(index: String) {
        stateHolder.onItemLongCLick(index)
    }

    override fun onFirstVisibleItemChange(position: Int) {
        stateHolder.onFirstVisibleItemChange(position)
    }

    override fun onPopupOpened() {
        stateHolder.onPopupOpened()
    }

    override fun onPopupClosed() {
        stateHolder.onPopupClosed()
    }

    override fun onAlphabetIndexClicked(position: Int) {
        stateHolder.onAlphabetIndexClicked(position)
    }

    override fun onTableContentIndexClicked(position: Int) {
        stateHolder.onTableContentIndexClicked(position)
    }

    override fun onTableContentClosed() {
        stateHolder.onTableContentClosed()
    }

    override fun onErrorButtonClick() {
        stateHolder.onErrorButtonClick()
    }

    override fun onCleared() {
        super.onCleared()
        stateHolder.onCleared()
    }

    private fun MonsterCompendiumState.asMonsterCompendiumViewState(): MonsterCompendiumViewState {
        return MonsterCompendiumViewState(
            loadingState = when {
                this.errorState != null -> LoadingScreenState.Error(NO_INTERNET_CONNECTION)
                this.isLoading -> LoadingScreenState.LoadingScreen
                else -> LoadingScreenState.Success
            },
            items = this.items.asState(),
            alphabet = this.alphabet,
            alphabetSelectedIndex = this.alphabetSelectedIndex,
            popupOpened = this.popupOpened,
            tableContent = this.tableContent.asState(),
            tableContentIndex = this.tableContentIndex,
            tableContentInitialIndex = this.tableContentInitialIndex,
            tableContentOpened = this.tableContentOpened,
            isShowingMonsterFolderPreview = this.isShowingMonsterFolderPreview,
        )
    }

    private fun MonsterCompendiumViewState.asMonsterCompendiumState(): MonsterCompendiumState {
        return MonsterCompendiumState(
            isShowingMonsterFolderPreview = this.isShowingMonsterFolderPreview,
        )
    }
}

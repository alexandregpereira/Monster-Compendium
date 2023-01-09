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

package br.alexandregpereira.hunter.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.event.monster.detail.MonsterDetailEventDispatcher
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.ShowFolderPreview
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.search.domain.SearchMonstersByNameUseCase
import br.alexandregpereira.hunter.search.ui.SearchViewState
import br.alexandregpereira.hunter.search.ui.changeMonsters
import br.alexandregpereira.hunter.search.ui.changeSearchValue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val searchMonstersByNameUseCase: SearchMonstersByNameUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val monsterDetailEventDispatcher: MonsterDetailEventDispatcher,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SearchViewState.Initial)
    val state: StateFlow<SearchViewState> = _state
    private val searchQuery = MutableStateFlow(_state.value.searchValue)

    init {
        searchQuery.debounce(500L)
            .flatMapConcat {
                searchMonstersByNameUseCase(state.value.searchValue)
            }
            .map { result ->
                result.size to result.asState()
            }
            .onEach { (totalResults, monsterRows) ->
                _state.value = state.value.changeMonsters(monsterRows, totalResults)
            }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    fun onSearchValueChange(value: String) {
        _state.value = state.value.changeSearchValue(value)
        searchQuery.value = value
    }

    fun onItemClick(index: String) {
        monsterDetailEventDispatcher.dispatchEvent(
            Show(index = index, indexes = listOf(index))
        )
    }

    fun onItemLongClick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview)
    }
}

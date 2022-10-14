/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val searchMonstersByNameUseCase: SearchMonstersByNameUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(SearchViewState.Initial)
    val state: StateFlow<SearchViewState> = _state
    private val searchQuery = MutableStateFlow(_state.value.searchValue)

    private val _action = MutableSharedFlow<SearchAction>()
    val action: SharedFlow<SearchAction> = _action

    init {
        searchQuery.debounce(500L)
            .flatMapConcat {
                delay(200)
                Log.d("onSearchValueChange", "onSearchValueChange: ")
                searchMonstersByNameUseCase(state.value.searchValue)
            }
            .map { result ->
                result.asState()
            }
            .onEach { monsters ->
                _state.value = state.value.changeMonsters(monsters)
            }
            .flowOn(dispatcher)
            .launchIn(viewModelScope)
    }

    fun onSearchValueChange(value: String) {
        _state.value = state.value.changeSearchValue(value)
        searchQuery.value = value
    }

    fun onItemClick(index: String) {
        viewModelScope.launch {
            _action.emit(SearchAction.NavigateToDetail(index))
        }
    }

    fun onItemLongClick(index: String) {
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(index))
        folderPreviewEventDispatcher.dispatchEvent(ShowFolderPreview())
    }
}

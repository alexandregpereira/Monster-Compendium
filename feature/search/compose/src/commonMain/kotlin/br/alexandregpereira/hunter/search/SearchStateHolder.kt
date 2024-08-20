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

package br.alexandregpereira.hunter.search

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.search.domain.SearchKey
import br.alexandregpereira.hunter.search.domain.SearchKeySymbolAnd
import br.alexandregpereira.hunter.search.domain.SearchMonstersByUseCase
import br.alexandregpereira.hunter.search.ui.SearchViewState
import br.alexandregpereira.hunter.state.UiModel
import br.alexandregpereira.hunter.strings.formatWithPlural
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
internal class SearchStateHolder(
    private val searchMonstersByNameUseCase: SearchMonstersByUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val analytics: SearchAnalytics,
    private val dispatcher: CoroutineDispatcher,
    private val appLocalization: AppReactiveLocalization,
) : UiModel<SearchViewState>(SearchViewState(searchLabel = appLocalization.getStrings().search)) {

    private val searchQuery = MutableStateFlow(state.value.searchValue.text)
    private var job: Job? = null

    init {
        searchQuery.debounce(800L)
            .onEach {
                search()
            }
            .launchIn(scope)

        observeLanguageChanges()

        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            search(clearCache = true)
        }.launchIn(scope)

        setState {
            copy(searchKeys = SearchKey.entries.filter { it != SearchKey.Name }.toState())
        }
    }

    private fun search(clearCache: Boolean = false) {
        setState { copy(isSearching = true) }
        job?.cancel()
        job = searchMonstersByNameUseCase(state.value.searchValue.text, clearCache)
            .cancellable()
            .map { result ->
                result.size to result.asState()
            }
            .onEach { (totalResults, monsterRows) ->
                setState {
                    copy(
                        monsterRows = monsterRows,
                        totalResults = totalResults,
                        searchResults = appLocalization.getStrings().run {
                            searchResultsSingular.formatWithPlural(
                                totalResults,
                                searchResultsPlural
                            )
                        }
                    )
                }
            }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
                delay(300)
                setState { copy(isSearching = false) }
            }
            .map { (totalResults, _) ->
                analytics.trackSearch(totalResults, searchQuery.value)
                delay(300)
                setState { copy(isSearching = false) }
            }
            .launchIn(scope)
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach { language ->
            val strings = language.getStrings()
            setState {
                copy(
                    searchLabel = strings.search,
                    searchResults = strings.searchResultsSingular.formatWithPlural(
                        state.value.totalResults,
                        strings.searchResultsPlural
                    )
                )
            }
        }.launchIn(scope)
    }

    fun onSearchValueChange(value: TextFieldValue) {
        setState { copy(searchValue = value) }
        searchQuery.value = value.text
    }

    fun onItemClick(index: String) {
        analytics.trackItemClick(index, searchQuery.value)
        monsterEventDispatcher.dispatchEvent(
            Show(index = index, indexes = listOf(index))
        )
    }

    fun onItemLongClick(index: String) {
        analytics.trackItemLongClick(index, searchQuery.value)
        folderPreviewEventDispatcher.dispatchEvent(FolderPreviewEvent.AddMonster(index))
    }

    fun onSearchKeyClick(searchKeyIndex: Int) {
        val currentSearchValue = state.value.searchValue
        val searchKey = state.value.searchKeys[searchKeyIndex]
        val newSearchValue = if (currentSearchValue.text.isBlank()) {
            searchKey.keyWithSymbols
        } else {
            "${currentSearchValue.text} $SearchKeySymbolAnd ${searchKey.keyWithSymbols}"
        }
        onSearchValueChange(TextFieldValue(newSearchValue, TextRange(newSearchValue.length)))
    }

    fun onAddClick() {
        val monsterIndexes = state.value.monsterRows.map { it.index }
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(monsterIndexes))
    }

    fun onScrollChanges(firstVisibleItemIndex: Int, firstVisibleItemScrollOffset: Int) {
        setState {
            copy(
                firstVisibleItemIndex = firstVisibleItemIndex,
                firstVisibleItemScrollOffset = firstVisibleItemScrollOffset
            )
        }
    }

    fun onSearchKeysScrollChanges(scrollOffset: Int) {
        setState { copy(searchKeysScrollOffset = scrollOffset) }
    }
}

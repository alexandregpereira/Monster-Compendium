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

import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEvent.AddMonster
import br.alexandregpereira.hunter.folder.preview.event.FolderPreviewEventDispatcher
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.monster.event.MonsterEvent.OnVisibilityChanges.Show
import br.alexandregpereira.hunter.monster.event.MonsterEventDispatcher
import br.alexandregpereira.hunter.monster.event.collectOnMonsterCompendiumChanges
import br.alexandregpereira.hunter.search.domain.SearchMonstersByUseCase
import br.alexandregpereira.hunter.search.ui.SearchViewState
import br.alexandregpereira.hunter.search.ui.changeSearchValue
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@Suppress("OPT_IN_USAGE")
@OptIn(FlowPreview::class)
internal class SearchStateHolder(
    private val searchMonstersByNameUseCase: SearchMonstersByUseCase,
    private val folderPreviewEventDispatcher: FolderPreviewEventDispatcher,
    private val monsterEventDispatcher: MonsterEventDispatcher,
    private val analytics: SearchAnalytics,
    private val dispatcher: CoroutineDispatcher,
    private val appLocalization: AppReactiveLocalization,
) : UiModel<SearchViewState>(SearchViewState(searchLabel = appLocalization.getStrings().search)) {

    private val searchQuery = MutableStateFlow(state.value.searchValue)

    init {
        searchQuery.debounce(500L)
            .flatMapConcat {
                search()
            }
            .launchIn(scope)

        observeLanguageChanges()

        monsterEventDispatcher.collectOnMonsterCompendiumChanges {
            search().launchIn(scope)
        }.launchIn(scope)
    }

    private fun search(): Flow<Unit> {
        return searchMonstersByNameUseCase(state.value.searchValue)
            .map { result ->
                result.size to result.asState()
            }
            .onEach { (totalResults, monsterRows) ->
                setState {
                    copy(
                        monsterRows = monsterRows,
                        totalResults = totalResults,
                        searchResults = appLocalization.getStrings().searchResults(totalResults)
                    )
                }
            }
            .flowOn(dispatcher)
            .catch {
                analytics.logException(it)
            }
            .map { (totalResults, _) ->
                analytics.trackSearch(totalResults, searchQuery.value)
            }
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow.onEach { language ->
            val strings = language.getStrings()
            setState {
                copy(
                    searchLabel = strings.search,
                    searchResults = strings.searchResults(state.value.totalResults)
                )
            }
        }.launchIn(scope)
    }

    fun onSearchValueChange(value: String) {
        setState { changeSearchValue(value) }
        searchQuery.value = value
    }

    fun onItemClick(index: String) {
        analytics.trackItemClick(index, searchQuery.value)
        monsterEventDispatcher.dispatchEvent(
            Show(index = index, indexes = listOf(index))
        )
    }

    fun onItemLongClick(index: String) {
        analytics.trackItemLongClick(index, searchQuery.value)
        folderPreviewEventDispatcher.dispatchEvent(AddMonster(index))
    }
}

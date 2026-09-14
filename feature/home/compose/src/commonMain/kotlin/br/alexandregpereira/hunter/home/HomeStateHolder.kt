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

package br.alexandregpereira.hunter.home

import br.alexandregpereira.hunter.domain.folder.GetMonsterFoldersUseCase
import br.alexandregpereira.hunter.domain.folder.GetRecentlyViewedMonstersUseCase
import br.alexandregpereira.hunter.event.v2.EventListener
import br.alexandregpereira.hunter.home.domain.GetHomeContentTotals
import br.alexandregpereira.hunter.home.domain.GetHomeExtraContentProgress
import br.alexandregpereira.hunter.home.event.HomeEvent
import br.alexandregpereira.hunter.home.ui.HomeSectionState
import br.alexandregpereira.hunter.localization.AppReactiveLocalization
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class HomeStateHolder(
    private val appLocalization: AppReactiveLocalization,
    private val homeIntentHandler: HomeIntentHandler,
    private val getHomeContentTotals: GetHomeContentTotals,
    private val getHomeExtraContentProgress: GetHomeExtraContentProgress,
    private val getMonsterFolders: GetMonsterFoldersUseCase,
    private val getRecentlyViewedMonsters: GetRecentlyViewedMonstersUseCase,
    private val homeEventListener: EventListener<HomeEvent>,
    private val analytics: HomeAnalytics,
    private val dispatcher: CoroutineDispatcher,
) : UiModel<HomeState>(
    HomeState(
        viewState = homeMockViewState.copy(
            sections = buildHomeSections(
                categories = null,
                extraContent = null,
                recentlyViewed = null,
                folders = null,
            ),
            isLoading = true,
        ),
        strings = appLocalization.getHomeStrings(),
    )
) {

    private var intentJob: Job? = null
    private var sectionsJob: Job? = null
    private var extraContentJob: Job? = null

    init {
        observeLanguageChanges()
        observeEvents()
        loadSections(invalidateCache = false)
    }

    fun onIntent(intent: HomeIntent) {
        analytics.trackIntent(intent)
        // An intent can keep collecting results while its screen is open, like the spell
        // compendium clicks. Those screens cover the Home, so when a new intent arrives the
        // previous screen is already closed and its collection can be cancelled.
        intentJob?.cancel()
        intentJob = featureScope.launch {
            homeIntentHandler.onIntent(intent)
        }
    }

    /**
     * Loads the local sections at once, updating the state only after every one of them is loaded.
     * The extra content is loaded in parallel, since it comes from the API and can take longer or
     * fail.
     * Only the first sections of each load are tracked, since the sections flows can emit again
     * without a new load, like when a monster is viewed.
     */
    private fun loadSections(invalidateCache: Boolean) {
        loadExtraContent(isReload = invalidateCache)

        sectionsJob?.cancel()
        var isSectionsLoadedTracked = false
        sectionsJob = combine(
            getHomeContentTotals(invalidateCache)
                .map { it.toCategoriesSection() }
                .keepCurrentSectionOnError(),
            getMonsterFolders()
                .map { it.toFoldersSection() }
                .keepCurrentSectionOnError(),
            getRecentlyViewedMonsters()
                .map { it.toRecentlyViewedSection() }
                .keepCurrentSectionOnError(),
            ::Triple,
        )
            .flowOn(dispatcher)
            .onEach { (categories, folders, recentlyViewed) ->
                val sections = setSections(
                    categories = categories,
                    recentlyViewed = recentlyViewed,
                    folders = folders,
                )
                if (isSectionsLoadedTracked.not()) {
                    isSectionsLoadedTracked = true
                    analytics.trackSectionsLoaded(sections, isReload = invalidateCache)
                }
            }
            .launchIn(scope)
    }

    /**
     * A failure keeps the extra content currently shown, or keeps it hidden if it was never loaded.
     * Only the first extra content of each load is tracked, like the other sections.
     */
    private fun loadExtraContent(isReload: Boolean) {
        extraContentJob?.cancel()
        var isExtraContentLoadedTracked = false
        extraContentJob = getHomeExtraContentProgress()
            .map { it.toExtraContentSection() }
            .flowOn(dispatcher)
            .catch { error -> analytics.logException(error) }
            .onEach { extraContent ->
                setSections(extraContent = extraContent)
                if (isExtraContentLoadedTracked.not()) {
                    isExtraContentLoadedTracked = true
                    analytics.trackExtraContentLoaded(extraContent, isReload = isReload)
                }
            }
            .launchIn(scope)
    }

    /**
     * Updates the given sections, keeping the sections currently shown for the ones not given, so
     * the sections loaded in parallel don't override each other.
     */
    private fun setSections(
        categories: HomeSectionState.Categories? = currentSection(),
        extraContent: HomeSectionState.ExtraContent? = currentSection(),
        recentlyViewed: HomeSectionState.RecentlyViewed? = currentSection(),
        folders: HomeSectionState.Folders? = currentSection(),
    ): List<HomeSectionState> {
        val sections = buildHomeSections(
            categories = categories,
            extraContent = extraContent,
            recentlyViewed = recentlyViewed,
            folders = folders,
        )
        setState {
            copy(
                viewState = viewState.copy(
                    sections = sections,
                    // Without the categories there is no content yet, like before the first sync
                    isLoading = categories == null,
                )
            )
        }
        return sections
    }

    private inline fun <reified T : HomeSectionState> currentSection(): T? {
        return state.value.viewState.sections.filterIsInstance<T>().firstOrNull()
    }

    /**
     * A section that fails to load keeps the section currently shown, or stays hidden if it was
     * never loaded. So one failure doesn't hide the other sections.
     */
    private inline fun <reified T : HomeSectionState> Flow<T?>.keepCurrentSectionOnError(): Flow<T?> {
        return catch { error ->
            analytics.logException(error)
            emit(currentSection())
        }
    }

    /**
     * The features dispatch [HomeEvent.OnContentChanged] when the content shown on the Home
     * changes, so the Home doesn't need to know every feature event.
     */
    private fun observeEvents() {
        homeEventListener.events
            .onEach { event ->
                when (event) {
                    HomeEvent.OnContentChanged -> loadSections(invalidateCache = true)
                }
            }
            .launchIn(scope)
    }

    private fun observeLanguageChanges() {
        appLocalization.languageFlow
            .onEach { language ->
                setState { copy(strings = language.getHomeStrings()) }
            }
            .launchIn(scope)
    }
}

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

package br.alexandregpereira.hunter.spell.compendium

import br.alexandregpereira.hunter.analytics.Analytics
import br.alexandregpereira.hunter.analytics.ScrollDepthTracker
import br.alexandregpereira.hunter.analytics.ScrollEvent
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.search.removeAccents
import br.alexandregpereira.hunter.spell.compendium.domain.GetSpellsUseCase
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.event.SpellResult
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import br.alexandregpereira.hunter.state.UiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import br.alexandregpereira.hunter.event.v2.EventListener as EventListenerV2

@OptIn(FlowPreview::class)
class SpellCompendiumStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getSpellsUseCase: GetSpellsUseCase,
    private val eventListener: EventListener<SpellCompendiumEvent>,
    private val resultDispatcher: EventDispatcher<SpellCompendiumResult>,
    private val spellRegistrationEventDispatcher: SpellRegistrationEventDispatcher,
    private val appLocalization: AppLocalization,
    private val analytics: Analytics,
    private val spellResultListener: EventListenerV2<SpellResult>,
) : UiModel<SpellCompendiumState>(SpellCompendiumState()),
    SpellCompendiumIntent {

    private val searchQuery = MutableStateFlow(state.value.searchText)
    private val originalSpellsGroupByLevel = mutableMapOf<String, List<SpellCompendiumItemState>>()
    private var strings: SpellCompendiumStrings = getSpellCompendiumStrings(appLocalization.getLanguage())
    private var spellResultJob: Job? = null
    private val scrollDepthTracker = ScrollDepthTracker()

    init {
        debounceSearch()
        observeEvents()
        if (state.value.isShowing && state.value.spellsGroupByLevel.isEmpty()) {
            fetch()
        }
    }

    private fun fetch(
        spellIndex: String? = null,
        selectedSpellIndexes: List<String> = emptyList(),
    ) {
        strings = getSpellCompendiumStrings(appLocalization.getLanguage())
        scrollDepthTracker.reset()
        setState {
            copy(
                title = strings.title,
                searchText = "",
                searchTextLabel = strings.searchLabel,
                isSearchOpened = false,
            )
        }
        getSpellsUseCase()
            .onEach { spells ->
                analytics.track(
                    eventName = "Spell Compendium - loaded",
                    params = mapOf("count" to spells.size.toString()),
                )
                val spellsGroupByLevel = spells.groupByLevel(selectedSpellIndexes)
                val compendiumIndex = spells.firstOrNull {
                    it.index == spellIndex
                }?.level?.let { level ->
                    spellsGroupByLevel.compendiumIndexOf(level)
                }
                originalSpellsGroupByLevel.clear()
                originalSpellsGroupByLevel.putAll(spellsGroupByLevel)
                setState {
                    copy(
                        spellsGroupByLevel = spellsGroupByLevel,
                        initialItemIndex = compendiumIndex?.takeIf { it >= 0 } ?: 0,
                    )
                }
            }
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    private fun debounceSearch() {
        searchQuery.debounce(500L)
            .map {
                it.removeAccents().trim()
            }
            .onEach { text ->
                setState {
                    val spellsGroupByLevel = if (text.isNotBlank()){
                        analytics.track(
                            eventName = "Spell Compendium - search text changed",
                            params = mapOf("text" to text),
                        )
                        val spellsFiltered = originalSpellsGroupByLevel.values.flatten()
                            .filter { it.name.removeAccents().contains(text, ignoreCase = true) }
                        mapOf(strings.searchResults(spellsFiltered.size) to spellsFiltered)
                    } else originalSpellsGroupByLevel

                    copy(spellsGroupByLevel = spellsGroupByLevel)
                }
            }
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    private fun observeEvents() {
        eventListener.events.onEach { event ->
            when (event) {
                is SpellCompendiumEvent.Show -> {
                    analytics.track(eventName = "Spell Compendium - opened")
                    observeSpellResultEvents()
                    fetch(
                        spellIndex = event.spellIndex,
                        selectedSpellIndexes = event.selectedSpellIndexes,
                    )
                    setState { copy(isShowing = true) }
                }
                is SpellCompendiumEvent.Hide -> onClose()
            }
        }.launchIn(scope)
    }

    override fun onSearchTextChange(text: String) {
        // Filtering replaces the whole list, so the depth reached over the previous one no longer
        // applies.
        scrollDepthTracker.reset()
        setState { copy(searchText = text) }
        searchQuery.value = text
    }

    override fun onSearchClick() {
        analytics.track(eventName = "Spell Compendium - search clicked")
        setState { copy(isSearchOpened = true) }
    }

    override fun onSearchClose() {
        setState { copy(isSearchOpened = false) }
        onSearchTextChange("")
    }

    /**
     * The item count comes from the list itself instead of the state, so it cannot disagree with
     * the indexes measured against it while a search is rebuilding the list.
     */
    override fun onVisibleItemsChange(
        firstVisibleItemIndex: Int,
        lastVisibleItemIndex: Int,
        itemsSize: Int,
    ) {
        scrollDepthTracker.onScroll(
            firstVisibleItemIndex = firstVisibleItemIndex,
            lastVisibleItemIndex = lastVisibleItemIndex,
            itemsSize = itemsSize,
        ).forEach { scrollEvent ->
            val eventName = when (scrollEvent) {
                is ScrollEvent.Started -> "Spell Compendium - scroll started"
                is ScrollEvent.DepthReached -> "Spell Compendium - scroll depth reached"
            }
            analytics.track(eventName = eventName, params = scrollEvent.params)
        }
    }

    override fun onSpellClick(spellIndex: String) {
        analytics.track(
            eventName = "Spell Compendium - spell clicked",
            params = mapOf("index" to spellIndex),
        )
        resultDispatcher.dispatchEvent(SpellCompendiumResult.OnSpellClick(spellIndex))
    }

    override fun onSpellLongClick(spellIndex: String) {
        analytics.track(
            eventName = "Spell Compendium - spell long clicked",
            params = mapOf("index" to spellIndex),
        )
        resultDispatcher.dispatchEvent(SpellCompendiumResult.OnSpellLongClick(spellIndex))
    }

    override fun onAddSpell() {
        analytics.track(eventName = "Spell Compendium - add spell clicked")
        spellRegistrationEventDispatcher.dispatchEvent(SpellRegistrationEvent.Show())
    }

    override fun onClose() {
        analytics.track(
            eventName = "Spell Compendium - closed",
            params = scrollDepthTracker.summary().params,
        )
        spellResultJob?.cancel()
        setState { copy(isShowing = false) }
    }

    private fun observeSpellResultEvents() {
        spellResultJob?.cancel()
        spellResultJob = spellResultListener.events.onEach { result ->
            when (result) {
                is SpellResult.OnAdded -> fetch(spellIndex = result.spellIndex)
                is SpellResult.OnChanged -> fetch(spellIndex = result.spellIndex)
            }
        }.launchIn(scope)
    }

    private fun List<Spell>.groupByLevel(
        selectedSpellIndexes: List<String>,
    ): Map<String, List<SpellCompendiumItemState>> {
        return groupBy {
            it.level.getSpellLevelText()
        }.mapValues { (_, spells) -> spells.asState(selectedSpellIndexes) }
    }

    private fun Int.getSpellLevelText(): String {
        return when (this) {
            0 -> strings.cantrips
            else -> strings.level(this)
        }
    }

    private fun Map<String, List<SpellCompendiumItemState>>.compendiumIndexOf(level: Int): Int {
        val list = mutableListOf<Any>()
        entries.forEach { (key, value) ->
            list.add(key)
            list.addAll(value)
        }

        return list.indexOfFirst {
            when (it) {
                is String -> it == level.getSpellLevelText()
                else -> false
            }
        }
    }
}

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
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.localization.AppLocalization
import br.alexandregpereira.hunter.search.removeAccents
import br.alexandregpereira.hunter.spell.compendium.domain.GetSpellsUseCase
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.spell.detail.event.SpellDetailResult
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEvent
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationEventDispatcher
import br.alexandregpereira.hunter.spell.registration.event.SpellRegistrationResult
import br.alexandregpereira.hunter.spell.registration.event.collectOnSaved
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
    private val spellRegistrationResultListener: EventListenerV2<SpellRegistrationResult>,
    private val spellDetailResultListener: EventListenerV2<SpellDetailResult>,
) : UiModel<SpellCompendiumState>(SpellCompendiumState()),
    SpellCompendiumIntent {

    private val searchQuery = MutableStateFlow(state.value.searchText)
    private val originalSpellsGroupByLevel = mutableMapOf<String, List<SpellCompendiumItemState>>()
    private var strings: SpellCompendiumStrings = getSpellCompendiumStrings(appLocalization.getLanguage())
    private var spellRegistrationResultJob: Job? = null
    private var spellDetailResultJob: Job? = null

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
        setState { copy(searchText = "", searchTextLabel = strings.searchLabel) }
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
                analytics.track(
                    eventName = "Spell Compendium - search text changed",
                    params = mapOf("text" to text),
                )
                setState {
                    val spellsGroupByLevel = if (text.isNotBlank()){
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
                    observeSpellRegistrationEvents()
                    observeSpellDetailEvents()
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
        setState { copy(searchText = text) }
        searchQuery.value = text
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
        analytics.track(eventName = "Spell Compendium - closed")
        spellRegistrationResultJob?.cancel()
        spellDetailResultJob?.cancel()
        setState { copy(isShowing = false) }
    }

    private fun observeSpellRegistrationEvents() {
        spellRegistrationResultJob?.cancel()
        spellRegistrationResultJob = spellRegistrationResultListener.collectOnSaved { result ->
            fetch(spellIndex = result.spellIndex)
        }.launchIn(scope)
    }

    private fun observeSpellDetailEvents() {
        spellDetailResultJob?.cancel()
        spellDetailResultJob = spellDetailResultListener.events.onEach { result ->
            when (result) {
                is SpellDetailResult.OnChanged -> fetch(spellIndex = result.spellIndex)
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

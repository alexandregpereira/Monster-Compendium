package br.alexandregpereira.hunter.spell.compendium

import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.event.EventDispatcher
import br.alexandregpereira.hunter.event.EventListener
import br.alexandregpereira.hunter.spell.compendium.domain.GetSpellsUseCase
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumEvent
import br.alexandregpereira.hunter.spell.compendium.event.SpellCompendiumResult
import br.alexandregpereira.hunter.state.MutableStateHolder
import br.alexandregpereira.hunter.state.ScopeManager
import br.alexandregpereira.hunter.state.StateHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(FlowPreview::class)
class SpellCompendiumStateHolder internal constructor(
    private val dispatcher: CoroutineDispatcher,
    private val getSpellsUseCase: GetSpellsUseCase,
    private val stateHandler: MutableStateHolder<SpellCompendiumState>,
    private val eventListener: EventListener<SpellCompendiumEvent>,
    private val resultDispatcher: EventDispatcher<SpellCompendiumResult>,
) : ScopeManager(),
    SpellCompendiumIntent,
    StateHolder<SpellCompendiumState> by stateHandler {

    private val searchQuery = MutableStateFlow(state.value.searchText)
    private val originalSpellsGroupByLevel = mutableMapOf<String, List<SpellCompendiumItemState>>()

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
        getSpellsUseCase()
            .onEach { spells ->
                val spellsGroupByLevel = spells.groupByLevel(selectedSpellIndexes)
                val compendiumIndex = spells.firstOrNull {
                    it.index == spellIndex
                }?.level?.let { level ->
                    spellsGroupByLevel.compendiumIndexOf(level)
                }
                originalSpellsGroupByLevel.clear()
                originalSpellsGroupByLevel.putAll(spellsGroupByLevel)
                stateHandler.setState {
                    copy(
                        spellsGroupByLevel = spellsGroupByLevel,
                        initialItemIndex = compendiumIndex?.takeIf { it >= 0 } ?: 0
                    )
                }
            }
            .flowOn(dispatcher)
            .launchIn(scope)
    }

    private fun debounceSearch() {
        searchQuery.debounce(500L)
            .onEach { text ->
                stateHandler.setState {
                    val spellsGroupByLevel = if (text.isNotBlank()){
                        val spellsFiltered = spellsGroupByLevel.values.flatten()
                            .filter { it.name.contains(text, ignoreCase = true) }
                        mapOf("${spellsFiltered.size} results" to spellsFiltered)
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
                    fetch(
                        spellIndex = event.spellIndex,
                        selectedSpellIndexes = event.selectedSpellIndexes,
                    )
                    stateHandler.setState { copy(isShowing = true) }
                }
                is SpellCompendiumEvent.Hide -> onClose()
            }
        }.launchIn(scope)
    }

    override fun onSearchTextChange(text: String) {
        stateHandler.setState { copy(searchText = text) }
        searchQuery.value = text
    }

    override fun onSpellClick(spellIndex: String) {
        resultDispatcher.dispatchEvent(SpellCompendiumResult.OnSpellClick(spellIndex))
    }

    override fun onSpellLongClick(spellIndex: String) {
        resultDispatcher.dispatchEvent(SpellCompendiumResult.OnSpellLongClick(spellIndex))
    }

    override fun onClose() {
        stateHandler.setState { copy(isShowing = false) }
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
            0 -> "Cantrip"
            else -> "Level $this"
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

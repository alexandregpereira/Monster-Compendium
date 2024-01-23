package br.alexandregpereira.hunter.spell.compendium.event

import br.alexandregpereira.hunter.event.EventResultDispatcher

sealed class SpellCompendiumEvent {

    data class Show(
        val spellIndex: String? = null,
        val selectedSpellIndexes: List<String> = emptyList(),
    ) : SpellCompendiumEvent()

    data object Hide : SpellCompendiumEvent()
}

sealed class SpellCompendiumResult {

    data class OnSpellClick(val spellIndex: String) : SpellCompendiumResult()

    data class OnSpellLongClick(val spellIndex: String) : SpellCompendiumResult()
}

interface SpellCompendiumEventResultDispatcher : EventResultDispatcher<SpellCompendiumEvent, SpellCompendiumResult>

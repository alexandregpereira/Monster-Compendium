package br.alexandregpereira.hunter.spell.compendium

import br.alexandregpereira.hunter.localization.Language

internal class SpellCompendiumEnStrings : SpellCompendiumStrings {
    override val searchResults: (Int) -> String = { count -> "$count results" }
    override val cantrips: String = "Cantrips"
    override val level: (Int) -> String = { level -> "Level $level" }
    override val searchLabel: String = "Search"
}

internal class SpellCompendiumPtStrings : SpellCompendiumStrings {
    override val searchResults: (Int) -> String = { count -> "$count resultados" }
    override val cantrips: String = "Truques"
    override val level: (Int) -> String = { level -> "${level}º Círculo" }
    override val searchLabel: String = "Buscar"
}

internal interface SpellCompendiumStrings {
    val searchResults: (Int) -> String
    val cantrips: String
    val level: (Int) -> String
    val searchLabel: String
}

internal fun getSpellCompendiumStrings(lang: Language): SpellCompendiumStrings {
    return when (lang) {
        Language.ENGLISH -> SpellCompendiumEnStrings()
        Language.PORTUGUESE -> SpellCompendiumPtStrings()
    }
}

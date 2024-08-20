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

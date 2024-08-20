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

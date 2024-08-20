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

data class SpellCompendiumState(
    val isShowing: Boolean = false,
    val spellsGroupByLevel: Map<String, List<SpellCompendiumItemState>> = emptyMap(),
    val searchText: String = "",
    val searchTextLabel: String = "",
    val initialItemIndex: Int = 0,
)

data class SpellCompendiumItemState(
    val index: String,
    val name: String,
    val school: SpellCompendiumSchoolOfMagicState,
    val selected: Boolean = false,
)

enum class SpellCompendiumSchoolOfMagicState {
    ABJURATION,
    CONJURATION,
    DIVINATION,
    ENCHANTMENT,
    EVOCATION,
    ILLUSION,
    NECROMANCY,
    TRANSMUTATION,
}

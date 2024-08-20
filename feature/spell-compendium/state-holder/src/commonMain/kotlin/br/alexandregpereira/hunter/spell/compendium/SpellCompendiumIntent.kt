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

interface SpellCompendiumIntent {
    fun onSearchTextChange(text: String)

    fun onSpellClick(spellIndex: String)

    fun onSpellLongClick(spellIndex: String)

    fun onClose()
}

class EmptySpellCompendiumIntent : SpellCompendiumIntent {

    override fun onSearchTextChange(text: String) {}

    override fun onSpellClick(spellIndex: String) {}

    override fun onSpellLongClick(spellIndex: String) {}

    override fun onClose() {}
}

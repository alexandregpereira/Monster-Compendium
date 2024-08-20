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

package br.alexandregpereira.hunter.data.spell.local.dao

import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity

interface SpellDao {

    suspend fun insert(entities: List<SpellEntity>)

    suspend fun getSpell(index: String): SpellEntity

    suspend fun deleteAll()

    suspend fun getSpells(indexes: List<String>): List<SpellEntity>

    suspend fun getSpells(): List<SpellEntity>

    suspend fun getSpellsEdited(): List<SpellEntity>
}

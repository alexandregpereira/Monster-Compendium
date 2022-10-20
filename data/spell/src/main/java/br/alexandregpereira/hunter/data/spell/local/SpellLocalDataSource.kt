/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.spell.local

import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import kotlinx.coroutines.flow.Flow

internal interface SpellLocalDataSource {

    fun saveSpells(spells: List<SpellEntity>): Flow<Unit>
    fun getSpell(index: String): Flow<SpellEntity>
    fun deleteSpells(): Flow<Unit>
}

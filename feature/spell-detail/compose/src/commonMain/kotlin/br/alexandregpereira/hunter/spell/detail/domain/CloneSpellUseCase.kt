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

package br.alexandregpereira.hunter.spell.detail.domain

import br.alexandregpereira.hunter.domain.spell.GetSpellUseCase
import br.alexandregpereira.hunter.domain.spell.SaveSpells
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus
import br.alexandregpereira.hunter.uuid.generateUUID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

internal fun interface CloneSpellUseCase {
    operator fun invoke(spellIndex: String, spellName: String): Flow<String>
}

@ExperimentalCoroutinesApi
internal class CloneSpellUseCaseImpl(
    private val getSpell: GetSpellUseCase,
    private val saveSpells: SaveSpells,
) : CloneSpellUseCase {

    override fun invoke(
        spellIndex: String,
        spellName: String
    ): Flow<String> {
        return getSpell(spellIndex).flatMapConcat { original ->
            val newIndex = "spell-$spellIndex-${generateUUID()}-k4k4sh1"
            saveSpells(listOf(original.copy(index = newIndex, name = spellName, status = SpellStatus.Cloned)))
                .map { newIndex }
        }
    }
}

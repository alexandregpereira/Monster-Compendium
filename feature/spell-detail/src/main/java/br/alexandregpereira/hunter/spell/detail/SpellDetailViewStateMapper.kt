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

package br.alexandregpereira.hunter.spell.detail

import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.spell.detail.ui.SavingThrowTypeState
import br.alexandregpereira.hunter.spell.detail.ui.SpellState
import br.alexandregpereira.hunter.ui.compose.SchoolOfMagicState

internal fun Spell.asState(): SpellState {
    return SpellState(
        index = index,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType?.let { SavingThrowTypeState.valueOf(it.name) },
        damageType = damageType,
        school = SchoolOfMagicState.valueOf(school.name),
        description = description,
        higherLevel = higherLevel
    )
}

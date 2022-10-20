/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.spell.remote.mapper

import br.alexandregpereira.hunter.data.spell.remote.model.SpellDto
import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell

internal fun List<SpellDto>.toDomain(): List<Spell> {
    return map {
        Spell(
            index = it.index,
            name = it.name,
            level = it.level,
            castingTime = it.castingTime,
            components = it.components,
            duration = it.duration,
            range = it.range,
            ritual = it.ritual,
            concentration = it.concentration,
            savingThrowType = it.savingThrowType ?.let { type -> SavingThrowType.valueOf(type.name) },
            damageType = it.damageType,
            school = SchoolOfMagic.valueOf(it.school.name),
            description = it.description,
            higherLevel = it.higherLevel
        )
    }
}

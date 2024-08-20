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

package br.alexandregpereira.hunter.data.spell.local.mapper

import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus

internal fun SpellEntity.toDomain(): Spell {
    return Spell(
        index = spellIndex,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType ?.let { type -> SavingThrowType.valueOf(type) },
        damageType = damageType,
        school = SchoolOfMagic.valueOf(school),
        description = description,
        higherLevel = higherLevel,
        status = when (status) {
            0 -> SpellStatus.Original
            else -> SpellStatus.Imported
        },
    )
}

fun List<Spell>.toEntity(): List<SpellEntity> {
    return map {
        SpellEntity(
            spellIndex = it.index,
            name = it.name,
            level = it.level,
            castingTime = it.castingTime,
            components = it.components,
            duration = it.duration,
            range = it.range,
            ritual = it.ritual,
            concentration = it.concentration,
            savingThrowType = it.savingThrowType?.name,
            damageType = it.damageType,
            school = it.school.name,
            description = it.description,
            higherLevel = it.higherLevel,
            status = when (it.status) {
                SpellStatus.Original -> 0
                SpellStatus.Imported -> 1
            },
        )
    }
}

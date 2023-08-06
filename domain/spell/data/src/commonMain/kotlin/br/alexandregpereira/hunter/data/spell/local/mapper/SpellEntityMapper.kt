/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.spell.local.mapper

import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell

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
        higherLevel = higherLevel
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
            higherLevel = it.higherLevel
        )
    }
}

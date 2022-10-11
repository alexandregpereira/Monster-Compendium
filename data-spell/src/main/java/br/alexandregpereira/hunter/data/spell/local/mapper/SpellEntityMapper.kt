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

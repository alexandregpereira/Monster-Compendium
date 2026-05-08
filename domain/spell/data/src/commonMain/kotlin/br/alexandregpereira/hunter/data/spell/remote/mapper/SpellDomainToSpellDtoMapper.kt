package br.alexandregpereira.hunter.data.spell.remote.mapper

import br.alexandregpereira.hunter.data.spell.remote.model.SavingThrowTypeDto
import br.alexandregpereira.hunter.data.spell.remote.model.SchoolOfMagicDto
import br.alexandregpereira.hunter.data.spell.remote.model.SpellDto
import br.alexandregpereira.hunter.domain.spell.model.Spell

internal fun List<Spell>.toDtos(): List<SpellDto> {
    return map { it.toDto() }
}

internal fun Spell.toDto(): SpellDto {
    return SpellDto(
        index = index,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType?.let { SavingThrowTypeDto.valueOf(it.name) },
        damageType = damageType,
        school = SchoolOfMagicDto.valueOf(school.name),
        description = description,
        higherLevel = higherLevel,
    )
}

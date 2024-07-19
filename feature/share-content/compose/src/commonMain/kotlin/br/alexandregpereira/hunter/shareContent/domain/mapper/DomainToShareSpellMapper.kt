package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpell

internal fun Spell.toShareSpell(): ShareSpell {
    return ShareSpell(
        index = index,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType?.name,
        damageType = damageType,
        school = school.name,
        description = description,
        higherLevel = higherLevel,
    )
}

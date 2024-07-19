package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.spell.model.SavingThrowType
import br.alexandregpereira.hunter.domain.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.spell.model.SpellStatus
import br.alexandregpereira.hunter.shareContent.domain.model.ShareSpell

internal fun ShareSpell.toSpell(): Spell {
    return Spell(
        index = index,
        name = name,
        level = level,
        castingTime = castingTime,
        components = components,
        duration = duration,
        range = range,
        ritual = ritual,
        concentration = concentration,
        savingThrowType = savingThrowType?.let { SavingThrowType.valueOf(it) },
        damageType = damageType,
        school = SchoolOfMagic.valueOf(school),
        description = description,
        higherLevel = higherLevel,
        status = SpellStatus.Imported,
    )
}

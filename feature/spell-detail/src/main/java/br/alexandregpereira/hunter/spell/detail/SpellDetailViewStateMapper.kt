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

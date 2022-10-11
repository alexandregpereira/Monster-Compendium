package br.alexandregpereira.hunter.data.monster.spell.remote.mapper

import br.alexandregpereira.hunter.data.monster.spell.remote.model.SpellcastingDto
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType

fun List<SpellcastingDto>.toDomain(): List<Spellcasting> {
    return map { dto ->
        Spellcasting(
            description = dto.desc,
            type = SpellcastingType.valueOf(dto.type.name),
            usages = dto.spellsByGroup.toDomain()
        )
    }
}

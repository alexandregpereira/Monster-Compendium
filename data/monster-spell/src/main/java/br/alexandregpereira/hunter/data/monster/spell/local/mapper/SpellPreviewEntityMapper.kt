package br.alexandregpereira.hunter.data.monster.spell.local.mapper

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellPreview

internal fun List<SpellPreviewEntity>.toDomain(): List<SpellPreview> {
    return map { it.toDomain() }
}

internal fun SpellPreviewEntity.toDomain(): SpellPreview {
    return SpellPreview(
        index = spellIndex,
        name = name,
        level = level,
        school = SchoolOfMagic.valueOf(school),
    )
}

internal fun List<SpellPreview>.toEntity(): List<SpellPreviewEntity> {
    return map {
        SpellPreviewEntity(
            spellIndex = it.index,
            name = it.name,
            level = it.level,
            school = it.school.name,
        )
    }
}

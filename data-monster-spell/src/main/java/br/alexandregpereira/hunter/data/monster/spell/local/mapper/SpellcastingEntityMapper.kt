package br.alexandregpereira.hunter.data.monster.spell.local.mapper

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.Spellcasting
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellcastingType

fun List<SpellcastingCompleteEntity>.toDomain(): List<Spellcasting> {
    return map { entity ->
        Spellcasting(
            description = entity.spellcasting.description,
            type = SpellcastingType.valueOf(entity.spellcasting.type),
            usages = entity.usages.toDomain()
        )
    }
}

fun List<Spellcasting>.toEntity(monsterIndex: String): List<SpellcastingCompleteEntity> {
    return map { spellcasting ->
        val spellcastingId = "${spellcasting.type}-$monsterIndex"
        SpellcastingCompleteEntity(
            spellcasting = SpellcastingEntity(
                spellcastingId = spellcastingId,
                type = spellcasting.type.name,
                description = spellcasting.description,
                monsterIndex = monsterIndex
            ),
            usages = spellcasting.usages.toEntity(spellcastingId)
        )
    }
}

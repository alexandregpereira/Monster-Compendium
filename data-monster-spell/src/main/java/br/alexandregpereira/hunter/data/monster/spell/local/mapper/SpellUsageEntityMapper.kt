package br.alexandregpereira.hunter.data.monster.spell.local.mapper

import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageEntity
import br.alexandregpereira.hunter.domain.monster.spell.model.SpellUsage

internal fun List<SpellUsageCompleteEntity>.toDomain(): List<SpellUsage> {
    return map { entity ->
        SpellUsage(
            group = entity.spellUsage.group,
            spells = entity.spells.toDomain()
        )
    }
}

internal fun List<SpellUsage>.toEntity(spellcastingId: String): List<SpellUsageCompleteEntity> {
    return map { usage ->
        SpellUsageCompleteEntity(
            spellUsage = SpellUsageEntity(
                spellUsageId = "${usage.group}-$spellcastingId",
                group = usage.group,
                spellcastingId = spellcastingId
            ),
            spells = usage.spells.toEntity()
        )
    }
}

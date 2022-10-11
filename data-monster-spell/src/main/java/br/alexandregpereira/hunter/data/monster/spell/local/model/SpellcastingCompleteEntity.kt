package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SpellcastingCompleteEntity(
    @Embedded val spellcasting: SpellcastingEntity,
    @Relation(
        entity = SpellUsageEntity::class,
        parentColumn = "spellcastingId",
        entityColumn = "spellUsageId",
        associateBy = Junction(SpellcastingSpellUsageCrossRefEntity::class),
    )
    val usages: List<SpellUsageCompleteEntity>
)

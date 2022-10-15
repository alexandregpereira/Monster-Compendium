package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SpellUsageCompleteEntity(
    @Embedded val spellUsage: SpellUsageEntity,
    @Relation(
        parentColumn = "spellUsageId",
        entityColumn = "spellIndex",
        associateBy = Junction(SpellUsageSpellCrossRefEntity::class),
    )
    val spells: List<SpellPreviewEntity>
)

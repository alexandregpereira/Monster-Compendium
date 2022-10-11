package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Entity

@Entity(primaryKeys = ["spellcastingId", "spellUsageId"])
data class SpellcastingSpellUsageCrossRefEntity(
    val spellcastingId: String,
    val spellUsageId: String
)

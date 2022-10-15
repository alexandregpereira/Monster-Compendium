package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Entity

@Entity(primaryKeys = ["spellUsageId", "spellIndex"])
data class SpellUsageSpellCrossRefEntity(
    val spellUsageId: String,
    val spellIndex: String
)

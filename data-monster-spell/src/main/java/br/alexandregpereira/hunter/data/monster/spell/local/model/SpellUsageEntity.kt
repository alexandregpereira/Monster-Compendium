package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpellUsageEntity(
    @PrimaryKey val spellUsageId: String,
    val group: String,
    val spellcastingId: String
)

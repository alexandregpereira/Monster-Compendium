package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpellcastingEntity(
    @PrimaryKey val spellcastingId: String,
    val type: String,
    val description: String,
    val monsterIndex: String
)

package br.alexandregpereira.hunter.data.monster.spell.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpellPreviewEntity(
    @PrimaryKey val spellIndex: String,
    val name: String,
    val level: Int,
    val school: String,
)

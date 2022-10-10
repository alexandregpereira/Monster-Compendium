package br.alexandregpereira.hunter.data.spell.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SpellEntity(
    @PrimaryKey val spellIndex: String,
    val name: String,
    val level: Int,
    val castingTime: String,
    val components: String,
    val duration: String,
    val range: String,
    val ritual: Boolean,
    val concentration: Boolean,
    val savingThrowType: String?,
    val damageType: String?,
    val school: String,
    val description: String,
    val higherLevel: String?
)

package br.alexandregpereira.hunter.data.monster.lore.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonsterLoreEntryEntity(
    @PrimaryKey
    val id: String,
    val title: String? = null,
    val description: String,
    val monsterIndex: String
)

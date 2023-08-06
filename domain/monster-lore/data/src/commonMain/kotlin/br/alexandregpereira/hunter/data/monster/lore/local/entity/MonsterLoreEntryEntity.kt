package br.alexandregpereira.hunter.data.monster.lore.local.entity

data class MonsterLoreEntryEntity(
    val id: String,
    val title: String? = null,
    val description: String,
    val monsterIndex: String
)

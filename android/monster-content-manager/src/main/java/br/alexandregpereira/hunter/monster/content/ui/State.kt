package br.alexandregpereira.hunter.monster.content.ui

data class MonsterContentState(
    val acronym: String,
    val name: String,
    val originalName: String?,
    val totalMonsters: Int,
    val summary: String,
    val coverImageUrl: String,
    val isEnabled: Boolean,
)

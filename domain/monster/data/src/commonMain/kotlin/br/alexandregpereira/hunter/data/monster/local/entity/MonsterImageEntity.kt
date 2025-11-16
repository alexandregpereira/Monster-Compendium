package br.alexandregpereira.hunter.data.monster.local.entity

data class MonsterImageEntity(
    val monsterIndex: String,
    val imageUrl: String,
    val backgroundColorLight: String,
    val backgroundColorDark: String,
    val isHorizontalImage: Boolean,
    val imageContentScale: Int?,
)

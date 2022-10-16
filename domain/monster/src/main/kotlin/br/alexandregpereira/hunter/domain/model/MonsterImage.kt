package br.alexandregpereira.hunter.domain.model

data class MonsterImage(
    val monsterIndex: String,
    val backgroundColor: Color,
    val isHorizontalImage: Boolean,
    val imageUrl: String,
)

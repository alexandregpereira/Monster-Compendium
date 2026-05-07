package br.alexandregpereira.hunter.data.monster.local.entity

data class MonsterImageEntity(
    val monsterIndex: String,
    val imageUrl: String?,
    val backgroundColorLight: String?,
    val backgroundColorDark: String?,
    val isHorizontalImage: Boolean?,
    val imageContentScale: Int?,
)

fun MonsterImageEntity.takeIfContentIsNotNull(): MonsterImageEntity? {
    return this.takeUnless {
        it.imageUrl == null &&
                it.backgroundColorLight == null &&
                it.backgroundColorDark == null &&
                it.isHorizontalImage == null &&
                it.imageContentScale == null
    }
}

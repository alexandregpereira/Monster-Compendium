package br.alexandregpereira.hunter.shareContent.domain.model

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.spell.model.Spell

internal data class ShareContent(
    val monsters: List<Monster>?,
    val monstersLore: List<MonsterLore>?,
    val spells: List<Spell>?,
    val monsterImages: List<MonsterImage>?,
    val minimumAppVersionCode: Int = CURRENT_MINIMUM_APP_VERSION_CODE,
) {
    companion object {
        const val CURRENT_MINIMUM_APP_VERSION_CODE = 258813
    }
}

internal fun List<MonsterLore>.appendMonsterName(
    monsters: List<Monster>,
): List<MonsterLore> {
    val monstersByIndex = monsters.associateBy { it.index }
    return map {
        it.copy(
            name = monstersByIndex[it.index]?.name ?: it.name,
        )
    }.sortByMonsterIndex(monsterIndexes = monsters.map { it.index })
}

internal fun List<MonsterLore>.sortByMonsterIndex(
    monsterIndexes: List<String>,
): List<MonsterLore> {
    val indexOrder = monsterIndexes.withIndex().associate { (i, monsterIndex) -> monsterIndex to i }
    return sortedBy { indexOrder[it.index] ?: Int.MAX_VALUE }
}

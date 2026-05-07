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

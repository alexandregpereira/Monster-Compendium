package br.alexandregpereira.hunter.shareContent.domain.model

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.spell.model.Spell

internal data class ShareContent(
    val monsters: List<Monster>? = null,
    val monstersLore: List<MonsterLore>? = null,
    val spells: List<Spell>? = null,
    val minimumAppVersionCode: Int = CURRENT_MINIMUM_APP_VERSION_CODE,
) {
    companion object {
        const val CURRENT_MINIMUM_APP_VERSION_CODE = 258813
    }
}

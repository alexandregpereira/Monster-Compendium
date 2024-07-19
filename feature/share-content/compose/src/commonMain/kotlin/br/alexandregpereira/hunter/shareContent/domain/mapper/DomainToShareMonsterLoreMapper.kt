package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLoreEntry

internal fun MonsterLore.toShareMonsterLore(): ShareMonsterLore {
    return ShareMonsterLore(
        index = index,
        name = name,
        entries = entries.map {
            ShareMonsterLoreEntry(
                title = it.title,
                description = it.description,
            )
        }
    )
}

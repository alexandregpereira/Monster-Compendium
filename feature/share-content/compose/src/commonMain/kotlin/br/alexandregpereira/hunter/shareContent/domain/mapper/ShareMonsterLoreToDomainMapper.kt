package br.alexandregpereira.hunter.shareContent.domain.mapper

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.shareContent.domain.model.ShareMonsterLore

internal fun ShareMonsterLore.toMonsterLore(): MonsterLore {
    return MonsterLore(
        index = index,
        name = "",
        entries = entries.map {
            MonsterLoreEntry(
                title = it.title,
                description = it.description,
            )
        }
    )
}

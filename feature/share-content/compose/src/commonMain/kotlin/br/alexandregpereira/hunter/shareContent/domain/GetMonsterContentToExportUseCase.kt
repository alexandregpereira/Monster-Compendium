package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun interface GetMonstersContentToExport {

    operator fun invoke(monsterIndexes: List<String>): Flow<String>
}

internal fun GetMonstersContentToExport(
    getMonsters: GetMonstersByIdsUseCase,
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase,
    getMonstersContentEditedToExport: GetMonstersContentEditedToExport,
): GetMonstersContentToExport = GetMonstersContentToExport { monsterIndexes ->
    if (monsterIndexes.isEmpty()) {
        getMonstersContentEditedToExport()
    } else {
        getMonsters(monsterIndexes).map { monsters ->
            monsters.getContentToExport(getMonstersLore, getSpellsByIds)
        }
    }
}

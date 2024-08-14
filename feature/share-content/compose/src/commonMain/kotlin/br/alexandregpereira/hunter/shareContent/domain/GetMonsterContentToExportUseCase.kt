package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal fun interface GetMonsterContentToExport {

    operator fun invoke(monsterIndex: String): Flow<String>
}

internal fun GetMonsterContentToExport(
    getMonster: GetMonsterUseCase,
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase
): GetMonsterContentToExport = GetMonsterContentToExport { monsterIndex ->
    getMonster(monsterIndex).map { monster ->
        listOf(monster).getContentToExport(getMonstersLore, getSpellsByIds)
    }
}

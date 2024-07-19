package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.GetMonsterLoreUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareMonster
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareSpell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.serialization.encodeToString

internal fun interface GetMonsterContentToExport {

    operator fun invoke(monsterIndex: String): Flow<String>
}

internal fun GetMonsterContentToExport(
    getMonster: GetMonsterUseCase,
    getMonsterLore: GetMonsterLoreUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase
): GetMonsterContentToExport = GetMonsterContentToExport { monsterIndex ->
    getMonster(monsterIndex).map { monster ->
        val monsterLore = getMonsterLore(monsterIndex).singleOrNull()
        val spells = getSpellsByIds(monster.getSpellIndexes()).single().takeIf { it.isNotEmpty() }

        val shareContent = ShareContent(
            monsters = listOf(monster.toShareMonster()),
            monstersLore = listOfNotNull(monsterLore?.toShareMonsterLore()),
            spells = spells?.map { it.toShareSpell() },
        )
        json.encodeToString(shareContent)
    }
}

private fun Monster.getSpellIndexes(): List<String> {
    return spellcastings.asSequence().map {
        it.usages
    }.flatten().map { it.spells }.flatten().map { it.index }.toList()
}

package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByStatus
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareMonster
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareMonsterLore
import br.alexandregpereira.hunter.shareContent.domain.mapper.toShareSpell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.serialization.encodeToString

internal fun interface GetMonstersContentToExport {

    operator fun invoke(): Flow<String>
}

internal fun GetMonstersContentToExport(
    getMonstersByStatus: GetMonstersByStatus,
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase
): GetMonstersContentToExport = GetMonstersContentToExport {
    val status = setOf(MonsterStatus.Edited, MonsterStatus.Clone, MonsterStatus.Imported)
    getMonstersByStatus(status).map { monsters ->
        monsters.getContentToExport(getMonstersLore, getSpellsByIds)
    }
}

internal suspend fun List<Monster>.getContentToExport(
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase
): String {
    val monsters = this
    val monsterIndexes = monsters.map { it.index }
    val monstersLore = getMonstersLore(monsterIndexes).singleOrNull()
    val spellIndexes = monsters.flatMap { it.getSpellIndexes() }
    val spells = getSpellsByIds(spellIndexes).single().takeIf { it.isNotEmpty() }

    val shareContent = ShareContent(
        monsters = monsters.map { it.toShareMonster() },
        monstersLore = monstersLore?.map { it.toShareMonsterLore() },
        spells = spells?.map { it.toShareSpell() },
    )
    return json.encodeToString(shareContent)
}

private fun Monster.getSpellIndexes(): List<String> {
    return spellcastings.asSequence().map {
        it.usages
    }.flatten().map { it.spells }.flatten().map { it.index }.toList()
}

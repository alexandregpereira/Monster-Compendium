package br.alexandregpereira.hunter.shareContent.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import br.alexandregpereira.hunter.shareContent.domain.model.ShareContent
import br.alexandregpereira.hunter.shareContent.domain.model.appendMonsterName
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull

internal fun interface GetMonstersShareContent {
    suspend operator fun invoke(monsterIndexes: List<String>): ShareContent
}

internal class GetMonstersShareContentUseCase(
    private val getMonsters: GetMonstersByIdsUseCase,
    private val getMonstersLore: GetMonstersLoreByIdsUseCase,
    private val getSpellsByIds: GetSpellsByIdsUseCase,
) : GetMonstersShareContent {

    override suspend fun invoke(monsterIndexes: List<String>): ShareContent {
        return getMonsters(monsterIndexes).map { monsters ->
            monsters.getShareContent(
                getMonstersLore,
                getSpellsByIds,
            )
        }.first()
    }

    private suspend fun List<Monster>.getShareContent(
        getMonstersLore: GetMonstersLoreByIdsUseCase,
        getSpellsByIds: GetSpellsByIdsUseCase,
    ): ShareContent {
        val monsters = this.changeMonsterImage()
        val monsterIndexes = monsters.map { it.index }
        val monsterImages = monsters.getCustomMonsterImages()
        val monstersLore = getMonstersLore(monsterIndexes).singleOrNull()
            ?.appendMonsterName(monsters)
        val spellIndexes = monsters.flatMap { it.getSpellIndexes() }
        val spells = getSpellsByIds(spellIndexes).single().takeIf { it.isNotEmpty() }

        return ShareContent(
            monsters = monsters,
            monstersLore = monstersLore,
            spells = spells,
            monsterImages = monsterImages,
        )
    }

    private fun List<Monster>.changeMonsterImage(): List<Monster> {
        return map {
            it.copy(
                imageData = it.originalImageData,
            )
        }
    }

    private fun List<Monster>.getCustomMonsterImages(): List<MonsterImage> {
        return mapNotNull {
            it.customMonsterImage
        }
    }

    private fun Monster.getSpellIndexes(): List<String> {
        return spellcastings.asSequence().flatMap {
            it.usages
        }.flatMap { it.spells }.map { it.index }.toList()
    }
}
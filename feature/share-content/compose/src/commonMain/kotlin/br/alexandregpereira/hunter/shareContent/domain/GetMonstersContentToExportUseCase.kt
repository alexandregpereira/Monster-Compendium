/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

internal fun interface GetMonstersContentEditedToExport {

    operator fun invoke(): Flow<String>
}

internal fun GetMonstersContentEditedToExport(
    getMonstersByStatus: GetMonstersByStatus,
    getMonstersLore: GetMonstersLoreByIdsUseCase,
    getSpellsByIds: GetSpellsByIdsUseCase
): GetMonstersContentEditedToExport = GetMonstersContentEditedToExport {
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

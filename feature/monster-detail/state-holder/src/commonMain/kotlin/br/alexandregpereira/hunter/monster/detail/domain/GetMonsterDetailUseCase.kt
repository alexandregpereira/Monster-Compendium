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

package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterStatus
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreStatus
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersAroundIndexUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class GetMonsterDetailUseCase internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val getMonstersUseCase: GetMonstersAroundIndexUseCase,
    private val getMonsterUseCase: GetMonsterUseCase,
    private val getMonstersByIds: GetMonstersByIdsUseCase,
    private val getMonstersLoreByIdsUseCase: GetMonstersLoreByIdsUseCase,
    private val getSpellsByIdsUseCase: GetSpellsByIdsUseCase,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        index: String,
        indexes: List<String>,
        invalidateCache: Boolean = false
    ): Flow<MonsterDetail> {
        return getMeasurementUnitUseCase().flatMapLatest { measurementUnit ->
            getMonsters(index, indexes, invalidateCache).map { monsters ->
                val monster = monsters.find { monster -> monster.index == index }
                    ?: throw RuntimeException("Monster not found")

                val (loreList, spells) = coroutineScope {
                    val loreListDeferred = async {
                        getMonstersLoreByIdsUseCase(monsters.map { it.index }).single()
                    }

                    val spellsDeferred = async {
                        monsters.map { it.spellcastings }
                            .takeIf { it.isNotEmpty() }
                            ?.reduce { acc, values -> acc + values }
                            ?.map { it.usages }
                            ?.takeIf { it.isNotEmpty() }
                            ?.reduce { acc, values -> acc + values }
                            ?.map { it.spells }
                            ?.takeIf { it.isNotEmpty() }
                            ?.reduce { acc, values -> acc + values }
                            ?.map { it.index }
                            ?.let { spellIndexes ->
                                getSpellsByIdsUseCase(spellIndexes).single()
                            }.orEmpty()
                    }

                    loreListDeferred.await() to spellsDeferred.await()
                }
                MonsterDetail(
                    monsterIndexSelected = monsters.indexOf(monster),
                    measurementUnit = measurementUnit,
                    monsters = monsters.appendLore(loreList).appendSpells(spells)
                )
            }
        }
    }

    private fun List<Monster>.appendLore(loreList: List<MonsterLore>): List<Monster> {
        return map { monsterWithLore ->
            val loreNullable = loreList.find {
                it.index == monsterWithLore.index
            }
            monsterWithLore.copy(
                lore = loreNullable?.let { lore ->
                    val firstEntryDescription = lore.entries.firstOrNull {
                        it.description.isNotBlank()
                    }?.description
                        ?.split("\n")
                        ?.firstOrNull()
                        ?.takeIf { it.isNotBlank() } ?: return@let null

                    val loreSize = 180
                    val ellipse = if (
                        lore.entries.size > 1 || firstEntryDescription.length >= loreSize
                    ) "..." else ""
                    val loreSummary = firstEntryDescription.substring(
                        startIndex = 0,
                        endIndex = loreSize.coerceAtMost(firstEntryDescription.length)
                    )
                    loreSummary + ellipse
                },
                status = when (loreNullable?.status) {
                    MonsterLoreStatus.Imported -> when (monsterWithLore.status) {
                        MonsterStatus.Original -> MonsterStatus.Edited
                        MonsterStatus.Edited,
                        MonsterStatus.Clone,
                        MonsterStatus.Imported -> monsterWithLore.status
                    }
                    MonsterLoreStatus.Original,
                    null -> monsterWithLore.status
                }
            )
        }
    }

    private fun List<Monster>.appendSpells(spells: List<Spell>): List<Monster> {
        return map { monstersWithSpells ->
            monstersWithSpells.copy(
                spellcastings = monstersWithSpells.spellcastings.mapNotNull { spellcasting ->
                    spellcasting.copy(
                        usages = spellcasting.usages.mapNotNull { spellUsage ->
                            spellUsage.copy(
                                spells = spellUsage.spells.mapNotNull { spellPreview ->
                                    spells.find { spellPreview.index == it.index }?.run {
                                        spellPreview.copy(
                                            name = name,
                                            level = level,
                                            school = SchoolOfMagic.valueOf(school.name)
                                        )
                                    }
                                }
                            ).takeIf { it.spells.isNotEmpty() }
                        }
                    ).takeIf { it.usages.isNotEmpty() }
                }
            )
        }
    }

    private fun getMonsters(
        index: String,
        indexes: List<String>,
        invalidateCache: Boolean = false
    ): Flow<List<Monster>> {
        return if (indexes.isEmpty()) {
            getMonstersUseCase(index, invalidateCache)
        } else if (indexes.size == 1) {
            getMonsterUseCase(index).map { listOf(it) }
        } else {
            getMonstersByIds(indexes)
        }
    }
}

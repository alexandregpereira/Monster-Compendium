/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.monster.detail.domain

import br.alexandregpereira.hunter.monster.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.monster.lore.GetMonstersLoreByIdsUseCase
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import br.alexandregpereira.hunter.domain.monster.spell.model.SchoolOfMagic
import br.alexandregpereira.hunter.domain.spell.GetSpellsByIdsUseCase
import br.alexandregpereira.hunter.domain.spell.model.Spell
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.zip

class GetMonsterDetailUseCase internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
    private val getMonsterUseCase: GetMonsterUseCase,
    private val getMonstersByIds: GetMonstersByIdsUseCase,
    private val getMonstersLoreByIdsUseCase: GetMonstersLoreByIdsUseCase,
    private val getSpellsByIdsUseCase: GetSpellsByIdsUseCase,
) {

    operator fun invoke(
        index: String,
        indexes: List<String>,
    ): Flow<MonsterDetail> {
        return getMonsters(index, indexes)
            .zip(getMeasurementUnitUseCase()) { monsters, measurementUnit ->
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

    private fun List<Monster>.appendLore(loreList: List<MonsterLore>): List<Monster> {
        return map { monsterWithLore ->
            monsterWithLore.copy(
                lore = loreList.find {
                    it.index == monsterWithLore.index
                }?.entries?.firstOrNull()?.description
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
    ): Flow<List<Monster>> {
        return if (indexes.isEmpty()) {
            getMonstersUseCase(index)
        } else if (indexes.size == 1) {
            getMonsterUseCase(index).map { listOf(it) }
        } else {
            getMonstersByIds(indexes)
        }
    }
}

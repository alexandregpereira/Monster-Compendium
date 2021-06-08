/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

typealias MonsterPair = Pair<MonsterPreview, MonsterPreview?>
typealias MonstersBySection = Map<MonsterSection, List<MonsterPair>>

class GetMonsterPreviewsBySectionUseCase internal constructor(
    private val syncMonstersUseCase: SyncMonstersUseCase,
    private val getMonstersUseCase: GetMonstersUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<MonstersBySection> {
        return syncMonstersUseCase().flatMapLatest { getMonstersUseCase() }
            .groupMonsters()
            .map {
                it.map { key, value ->
                    key to value.toMonsterPairs()
                }
            }
    }

    private fun Flow<List<Monster>>.groupMonsters(): Flow<Map<MonsterSection, List<Monster>>> {
        return this.map { monsters ->
            val map = linkedMapOf<MonsterSection, MutableList<Monster>>()
            val letterGroupCountMap = hashMapOf<String, Int>()
            var previousSection: MonsterSection? = null
            var previousSectionHasGroup = false
            monsters.forEach { monster ->
                val monsterSection = if (monster.group != null) {
                    MonsterSection(
                        title = monster.group,
                        parentTitle = getParentTitle(
                            title = monster.group,
                            monsterSection = previousSection
                        ),
                    )
                } else {
                    val group = monster.getFirstLetter()

                    val letterGroupCount = letterGroupCountMap.getOrPut(group) { 0 }
                    if (previousSectionHasGroup) {
                        letterGroupCountMap[group] = letterGroupCount + 1
                    }

                    MonsterSection(
                        id = group + letterGroupCountMap[group],
                        title = group,
                        isHeader = isHeader(
                            title = group,
                            previousSection = previousSection,
                        )
                    )
                }
                map.getOrPut(monsterSection) { mutableListOf() }.run {
                    add(monster)
                }
                previousSection = monsterSection
                previousSectionHasGroup = monster.group != null
            }
            map
        }
    }

    private fun getParentTitle(
        title: String,
        monsterSection: MonsterSection?
    ): String? {
        return if (
            isParentTitleEligible(title, previousSection = monsterSection)
        ) {
            title.getFirstLetter().toString()
        } else {
            null
        }
    }

    private fun isParentTitleEligible(
        title: String,
        previousSection: MonsterSection?
    ): Boolean {
        return previousSection == null
                || title.getFirstLetter() != previousSection.title.getFirstLetter()
                || (title == previousSection.title
                && title.getFirstLetter() == previousSection.parentTitle?.getFirstLetter())
    }

    private fun isHeader(
        title: String,
        previousSection: MonsterSection?,
    ): Boolean {
        return previousSection == null
                || title.getFirstLetter() != previousSection.title.getFirstLetter()
                || previousSection.isHeader
    }

    private fun Monster.getFirstLetter(): String {
        return this.name.getFirstLetter().toString()
    }

    private fun String.getFirstLetter(): Char {
        return this.first().toUpperCase()
    }

    private fun List<Monster>.toMonsterPairs(): List<MonsterPair> {
        val map: LinkedHashMap<Monster, Monster?> = linkedMapOf()
        var lastMonsterHorizontalIndex = -1
        var mod = 0
        val totalMonsters = this.size
        this.forEachIndexed { index, monster ->
            if ((index + mod) % 2 == 0) {
                if (monster.imageData.isHorizontal &&
                    isIndexEligibleToBeHorizontal(index, lastMonsterHorizontalIndex, totalMonsters)
                ) {
                    lastMonsterHorizontalIndex = index
                    ++mod
                }
                map[monster] = null
            } else {
                val lastIndex = index - 1
                val lastMonster = this[lastIndex]
                map[lastMonster] = monster
            }
        }
        return map.toList().map { it.first.preview to it.second?.preview }
    }

    private fun isIndexEligibleToBeHorizontal(
        currentIndex: Int,
        lastMonsterHorizontalIndex: Int,
        totalMonsters: Int
    ): Boolean {
        return (lastMonsterHorizontalIndex == -1 && currentIndex < (totalMonsters - 2)) ||
                ((currentIndex - lastMonsterHorizontalIndex) >= HORIZONTAL_IMAGE_INTERVAL &&
                        currentIndex < (totalMonsters - 2))
    }
}

private const val HORIZONTAL_IMAGE_INTERVAL = 2

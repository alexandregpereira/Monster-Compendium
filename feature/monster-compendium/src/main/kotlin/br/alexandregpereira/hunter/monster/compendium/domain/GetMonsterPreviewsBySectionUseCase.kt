/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.monster.compendium.domain

import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.exception.NoMonstersException
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.sync.HandleLanguageUseCase
import br.alexandregpereira.hunter.domain.sync.SyncUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retry

typealias MonstersBySection = Map<MonsterSection, List<MonsterPreview>>

class GetMonsterPreviewsBySectionUseCase @Inject internal constructor(
    private val sync: SyncUseCase,
    private val getMonstersUseCase: GetMonsterPreviewsUseCase,
    private val handleLanguageUseCase: HandleLanguageUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<MonstersBySection> {
        return handleLanguageUseCase()
            .flatMapLatest { getMonstersUseCase() }
            .flatMapLatest { monsters ->
                if (monsters.isEmpty()) {
                    sync().map {
                        throw NoMonstersException()
                    }
                } else flowOf(monsters)
            }
            .retry(retries = 1) { cause: Throwable ->
                cause is NoMonstersException
            }
            .groupMonsters()
            .map { monstersBySection ->
                monstersBySection.map { section, monsters ->
                    section to monsters.appendIsHorizontal().map { it.preview }
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
                val monsterGroup = monster.group
                val monsterSection = if (monsterGroup != null) {
                    MonsterSection(
                        title = monsterGroup,
                        parentTitle = getParentTitle(
                            title = monsterGroup,
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
        return this.first().uppercaseChar()
    }

    private fun List<Monster>.appendIsHorizontal(): List<Monster> {
        val monsterRowsMap: LinkedHashMap<Monster, Monster?> = linkedMapOf()
        var lastMonsterHorizontalIndex = -1
        var mod = 0
        val totalMonsters = this.size
        this.mapIndexed { index, monster ->
            if ((index + mod) % 2 == 0) {
                if (monster.imageData.isHorizontal &&
                    isIndexEligibleToBeHorizontal(index, lastMonsterHorizontalIndex, totalMonsters)
                ) {
                    lastMonsterHorizontalIndex = index
                    ++mod
                }
                monsterRowsMap[monster] = null
            } else {
                val lastIndex = index - 1
                val lastMonster = this[lastIndex]
                monsterRowsMap[lastMonster] = monster
            }
        }
        return this.map { monster ->
            val isHorizontal = monsterRowsMap.containsKey(monster) && monsterRowsMap[monster] == null
            monster.changeIsHorizontalImage(isHorizontal = isHorizontal)
        }
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

    private fun Monster.changeIsHorizontalImage(isHorizontal: Boolean): Monster {
        return copy(preview = preview.copy(imageData = imageData.copy(isHorizontal = isHorizontal)))
    }
}

private const val HORIZONTAL_IMAGE_INTERVAL = 2

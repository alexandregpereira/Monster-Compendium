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

import br.alexandregpereira.hunter.domain.MonsterRepository
import br.alexandregpereira.hunter.domain.collections.map
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

typealias MonsterPair = Map<Monster, Monster?>
typealias MonstersBySection = Map<MonsterSection, MonsterPair>

class GetMonstersBySectionUseCase(
    private val repository: MonsterRepository
) {

    operator fun invoke(): Flow<MonstersBySection> {
        return repository.getMonsters().map { monsters ->
            var index = 0
            monsters.groupBy { monster ->
                monster.group?.let {
                    index += 1
                    MonsterSection(title = it, showTitle = true)
                } ?: MonsterSection(title = index.toString(), showTitle = false)
            }
        }.map {
            it.map { key, value ->
                key to value.toMonsterPair()
            }
        }
    }

    private fun List<Monster>.toMonsterPair(): MonsterPair {
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
        return map
    }

    private fun isIndexEligibleToBeHorizontal(
        currentIndex: Int,
        lastMonsterHorizontalIndex: Int,
        totalMonsters: Int
    ): Boolean {
        return lastMonsterHorizontalIndex == -1 ||
                ((currentIndex - lastMonsterHorizontalIndex ) >= HORIZONTAL_IMAGE_INTERVAL &&
                        currentIndex < (totalMonsters - 2))
    }
}

private const val HORIZONTAL_IMAGE_INTERVAL = 2

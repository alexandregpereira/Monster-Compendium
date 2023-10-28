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

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.isComplete
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single

class GetMonstersAroundIndexUseCase internal constructor(
    private val getMonsterPreviewsCacheUseCase: GetMonsterPreviewsCacheUseCase,
    private val getMonstersByIdsUseCase: GetMonstersByIdsUseCase
) {

    operator fun invoke(
        monsterIndex: String,
        invalidateCache: Boolean = false
    ): Flow<List<Monster>> = flow {
        val monsterPreviews = getMonsterPreviewsCacheUseCase(invalidateCache).sortMonstersByNameAndGroup().single()
        val position = monsterPreviews.indexOfFirst { it.index == monsterIndex }
        val completeMonsters = getCompleteMonsters(
            monsterPreviews = monsterPreviews,
            monsterPosition = position,
            monsterPagerScrollLimit = 5
        )
        emit(completeMonsters)

        emit(
            getCompleteMonsters(
                monsterPreviews = completeMonsters,
                monsterPosition = position,
                monsterPagerScrollLimit = 50,
                excludeMonsters = completeMonsters.filter { it.isComplete() }
            )
        )
    }

    private suspend fun getCompleteMonsters(
        monsterPreviews: List<Monster>,
        monsterPosition: Int,
        monsterPagerScrollLimit: Int,
        excludeMonsters: List<Monster> = emptyList()
    ): List<Monster> {
        val monsterIndexes = monsterPreviews.map { it.index }

        val fromIndex = monsterPosition - monsterPagerScrollLimit
        val toIndex = monsterPosition + monsterPagerScrollLimit + 1
        val monsterIndexesSubList = monsterIndexes.subList(
            fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(monsterPreviews.size)
        ).toMutableList().apply { removeAll(excludeMonsters.map { it.index }) }

        return getMonstersByIdsUseCase(monsterIndexesSubList).single().toSet()
            .let { completeMonsters ->
                val newMonsters = monsterPreviews.toMutableList()
                completeMonsters.forEach { completeMonster ->
                    newMonsters[monsterPreviews.indexOfFirst {
                        it.index == completeMonster.index
                    }] = completeMonster
                }
                newMonsters
            }
    }
}

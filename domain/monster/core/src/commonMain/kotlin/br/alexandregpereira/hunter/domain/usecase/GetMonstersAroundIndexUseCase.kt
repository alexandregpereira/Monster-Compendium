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

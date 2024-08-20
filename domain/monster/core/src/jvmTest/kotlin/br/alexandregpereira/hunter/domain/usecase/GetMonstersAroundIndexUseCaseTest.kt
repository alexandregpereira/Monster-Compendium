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

import br.alexandregpereira.hunter.domain.model.AbilityScore
import br.alexandregpereira.hunter.domain.model.AbilityScoreType.CHARISMA
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.isComplete
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GetMonstersAroundIndexUseCaseTest {

    private val getMonsterPreviewsCacheUseCase: GetMonsterPreviewsCacheUseCase = mockk()
    private val getMonstersByIdsUseCase: GetMonstersByIdsUseCase = mockk()
    private val useCase = GetMonstersAroundIndexUseCase(
        getMonsterPreviewsCacheUseCase = getMonsterPreviewsCacheUseCase,
        getMonstersByIdsUseCase = getMonstersByIdsUseCase,
    )

    private val totalRange = 1..200
    private val firstReturnRange = 95..105
    private val secondReturnRange = 50..150

    @Test
    operator fun `invoke`() = runBlocking {
        val monstersCached = (totalRange).map {
            Monster(index = it.toString())
        }
        val monstersByIdFirstTime = (firstReturnRange).map {
            Monster(
                index = it.toString(),
                abilityScores = listOf(AbilityScore(type = CHARISMA, value = 10, modifier = 0))
            )
        }
        val monstersByIdSecondTime = (secondReturnRange).mapNotNull { position ->
            if (position in firstReturnRange) {
                return@mapNotNull null
            }
            Monster(
                index = position.toString(),
                abilityScores = listOf(AbilityScore(type = CHARISMA, value = 10, modifier = 0))
            )
        }
        every { getMonsterPreviewsCacheUseCase() } returns flowOf(monstersCached)
        every { getMonstersByIdsUseCase(monstersByIdFirstTime.map { it.index }) } returns flowOf(
            monstersByIdFirstTime
        )
        every { getMonstersByIdsUseCase(monstersByIdSecondTime.map { it.index }) } returns flowOf(
            monstersByIdSecondTime
        )

        val result = useCase(monsterIndex = "100").toList().map {
            it.map { monster -> monster.index to monster.isComplete() }
        }

        val firstExpected = monstersCached.map {
            it.index to (it.index.toInt() in firstReturnRange)
        }
        val secondExpected = monstersCached.map {
            it.index to (it.index.toInt() in secondReturnRange)
        }

        assertEquals(expected = 2, actual = result.size)
        assertEquals(expected = firstExpected, actual = result.first())
        assertEquals(expected = secondExpected, actual = result.last())
    }
}

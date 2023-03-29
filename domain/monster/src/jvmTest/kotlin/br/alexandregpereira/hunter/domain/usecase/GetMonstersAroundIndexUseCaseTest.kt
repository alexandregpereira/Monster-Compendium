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

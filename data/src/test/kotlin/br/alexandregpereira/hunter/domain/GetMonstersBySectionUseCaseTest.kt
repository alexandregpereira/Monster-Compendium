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

package br.alexandregpereira.hunter.domain

import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMonstersBySectionUseCaseTest {

    private val repository = mockk<MonsterRepository>()
    private val useCase = GetMonstersBySectionUseCase(repository)

    @Test
    fun invoke() = runBlocking {
        // Given
        val monsters = (0..10).map {
            createMonster(
                index = it.toString(),
                group = if (it in 3..4) "Group" else if (it == 6) "Group1" else null
            )
        }
        every { repository.getMonsters() } returns flowOf(monsters)

        // When
        val result = useCase().single()

        // Then
        val monsterSectionExpected1 = MonsterSection(title = "0", showTitle = false)
        val monsterSectionExpected2 = MonsterSection(title = "Group", showTitle = true)
        val monsterSectionExpected3 = MonsterSection(title = "2", showTitle = false)
        val monsterSectionExpected4 = MonsterSection(title = "Group1", showTitle = true)
        val monsterSectionExpected5 = MonsterSection(title = "3", showTitle = false)
        assertEquals(5, result.size)
        assertEquals(monsterSectionExpected1, result.keys.first())
        assertEquals(monsterSectionExpected2, result.keys.toList()[1])
        assertEquals(monsterSectionExpected3, result.keys.toList()[2])
        assertEquals(monsterSectionExpected4, result.keys.toList()[3])
        assertEquals(monsterSectionExpected5, result.keys.toList()[4])

        assertEquals(listOf(monsters[0], monsters[2]), result[monsterSectionExpected1]!!.keys.toList())
        assertEquals(listOf(monsters[1], null), result[monsterSectionExpected1]!!.values.toList())

        assertEquals(listOf(monsters[3]), result[monsterSectionExpected2]!!.keys.toList())
        assertEquals(listOf(monsters[4]), result[monsterSectionExpected2]!!.values.toList())

        assertEquals(listOf(monsters[5]), result[monsterSectionExpected3]!!.keys.toList())
        assertEquals(listOf(null), result[monsterSectionExpected3]!!.values.toList())

        assertEquals(listOf(monsters[6]), result[monsterSectionExpected4]!!.keys.toList())
        assertEquals(listOf(null), result[monsterSectionExpected4]!!.values.toList())

        assertEquals(listOf(monsters[7], monsters[9]), result[monsterSectionExpected5]!!.keys.toList())
        assertEquals(listOf(monsters[8], monsters[10]), result[monsterSectionExpected5]!!.values.toList())
    }

    private fun createMonster(
        index: String,
        group: String? = null
    ): Monster {
        return Monster(
            index = index,
            type = MonsterType.ABERRATION,
            subtype = null,
            group = group,
            challengeRating = 0.0f,
            name = "",
            subtitle = "",
            imageData = MonsterImageData(
                url = "",
                backgroundColor = ""
            ),
            size = "",
            alignment = "",
            armorClass = 0,
            hitPoints = 0,
            hitDice = "",
            speed = Speed(hover = false, values = listOf())
        )
    }
}
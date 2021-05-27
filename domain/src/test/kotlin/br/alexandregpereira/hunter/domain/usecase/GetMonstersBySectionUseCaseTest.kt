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
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Stats
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
        val monsters = (0..24).map {
            createMonster(
                index = it.toString(),
                group = if (it == 1 || it in 3..4) "Group" else if (it == 8) "Group1" else if (it in 9..10) "Group2" else null,
                isHorizontal = it != 0 && it != 9
            )
        }
        every { repository.getMonsters() } returns flowOf(monsters)

        // When
        val result = useCase().single()

        // Then
        val monsterSectionExpected1 = MonsterSection(title = "0", showTitle = false)
        val monsterSectionExpected2 = MonsterSection(title = "Group", showTitle = true)
        val monsterSectionExpected3 = MonsterSection(title = "1", showTitle = false)
        val monsterSectionExpected4 = MonsterSection(title = "Group1", showTitle = true)
        val monsterSectionExpected5 = MonsterSection(title = "Group2", showTitle = true)
        val monsterSectionExpected6 = MonsterSection(title = "3", showTitle = false)
        assertEquals(monsters.size, result.toMonsters().size)
        assertEquals(6, result.size)
        assertEquals(monsterSectionExpected1, result.keys.first())
        assertEquals(monsterSectionExpected2, result.keys.toList()[1])
        assertEquals(monsterSectionExpected3, result.keys.toList()[2])
        assertEquals(monsterSectionExpected4, result.keys.toList()[3])
        assertEquals(monsterSectionExpected5, result.keys.toList()[4])
        assertEquals(monsterSectionExpected6, result.keys.toList()[5])

        assertEquals(
            listOf(
                monsters[0] to null,
            ),
            result[monsterSectionExpected1]
        )

        assertEquals(
            listOf(
                monsters[1] to null,
                monsters[3] to monsters[4]
            ),
            result[monsterSectionExpected2]
        )

        assertEquals(
            listOf(
                monsters[2] to null,
                monsters[5] to monsters[6],
                monsters[7] to null,
            ),
            result[monsterSectionExpected3]
        )

        assertEquals(
            listOf(
                monsters[8] to null
            ),
            result[monsterSectionExpected4]
        )

        assertEquals(
            listOf(
                monsters[9] to monsters[10]
            ),
            result[monsterSectionExpected5]
        )

        assertEquals(
            listOf(
                monsters[11] to null,
                monsters[12] to monsters[13],
                monsters[14] to null,
                monsters[15] to monsters[16],
                monsters[17] to null,
                monsters[18] to monsters[19],
                monsters[20] to null,
                monsters[21] to monsters[22],
                monsters[23] to monsters[24],
            ),
            result[monsterSectionExpected6]
        )
    }

    private fun createMonster(
        index: String,
        group: String? = null,
        isHorizontal: Boolean = true
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
                backgroundColor = Color(light = "", dark = ""),
                isHorizontal = isHorizontal
            ),
            size = "",
            alignment = "",
            stats = Stats(
                armorClass = 0,
                hitPoints = 0,
                hitDice = "",
            ),
            speed = Speed(hover = false, values = listOf()),
            abilityScores = listOf(),
            savingThrows = listOf(),
            skills = listOf(),
            damageVulnerabilities = listOf(),
            damageResistances = listOf(),
            damageImmunities = listOf(),
            conditionImmunities = listOf(),
            senses = listOf(),
            languages = "",
            specialAbilities = listOf(),
            actions = listOf()
        )
    }

    private fun MonstersBySection.toMonsters(): List<Monster> {
        val monsters = mutableListOf<Monster>()
        this.toList().map { it.second }.reduce { acc, list -> acc + list }.toList()
            .forEach { pair ->
                monsters.add(pair.first)
                pair.second?.let { monsters.add(it) }
            }
        return monsters
    }
}
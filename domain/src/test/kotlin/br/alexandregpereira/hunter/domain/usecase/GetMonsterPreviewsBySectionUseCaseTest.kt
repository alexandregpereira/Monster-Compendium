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
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.Stats
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMonsterPreviewsBySectionUseCaseTest {

    private val repository = mockk<MonsterRepository>()
    private val syncMonstersUseCase: SyncMonstersUseCase = mockk()
    private val useCase = GetMonsterPreviewsBySectionUseCase(syncMonstersUseCase, repository)

    private fun createMonsters(): List<Monster> {
        return (0..23).map {
            val name = when (it) {
                2 -> "Aztest$it"
                in 5..6 -> "Aztest$it"
                in 0..6 -> "A1test$it"
                in 7..11 -> "B1test$it"
                in 12..14 -> "C1test$it"
                in 15..18 -> "D1test$it"
                22 -> "Hztest$it"
                else -> "Hatest$it"
            }
            createMonster(
                index = it.toString(),
                name = name,
                group = if (it == 1 || it in 3..4) "Any" else if (it == 10) "Group1" else if (it in 11..12) "Group2"  else if (it in 19..20) "Hags"  else if (it == 23) "Hxgs1" else null,
                isHorizontal = it != 0 && it != 7
            )
        }
    }

    @Test
    fun invoke() = runBlocking {
        // Given
        val monsters = createMonsters().map { it.preview }
        every { syncMonstersUseCase() } returns flowOf(Unit)
        every { repository.getLocalMonsters() } returns flowOf(createMonsters())

        // When
        val result = useCase().single()

        // Then
        val monsterSectionExpected1 = MonsterSection(title = "A", id= "A0", isHeader = true)
        val monsterSectionExpected2 = MonsterSection(title = "Any", parentTitle = null)
        val monsterSectionExpected3 = MonsterSection(title = "A", id= "A1", isHeader = false)
        val monsterSectionExpected4 = MonsterSection(title = "B", id= "B0", isHeader = true)
        val monsterSectionExpected5 = MonsterSection(title = "C", id= "C0", isHeader = true)
        val monsterSectionExpected6 = MonsterSection(title = "D", id= "D0", isHeader = true)
        val monsterSectionExpected7 = MonsterSection(title = "Group1", parentTitle = "G")
        val monsterSectionExpected8 = MonsterSection(title = "Group2", parentTitle = null)
        val monsterSectionExpected9 = MonsterSection(title = "Hags", parentTitle = "H")
        val monsterSectionExpected10 = MonsterSection(title = "H", id= "H1", isHeader = false)
        val monsterSectionExpected11 = MonsterSection(title = "Hxgs1", parentTitle = null)
        val monsterSectionExpected12 = MonsterSection(title = "H", id= "H2", isHeader = false)
        assertEquals(monsters.size, result.toMonsters().size)
        assertEquals(12, result.size)
        assertEquals(monsterSectionExpected1, result.keys.first())
        assertEquals(monsterSectionExpected2, result.keys.toList()[1])
        assertEquals(monsterSectionExpected3, result.keys.toList()[2])
        assertEquals(monsterSectionExpected4, result.keys.toList()[3])
        assertEquals(monsterSectionExpected5, result.keys.toList()[4])
        assertEquals(monsterSectionExpected6, result.keys.toList()[5])
        assertEquals(monsterSectionExpected7, result.keys.toList()[6])
        assertEquals(monsterSectionExpected8, result.keys.toList()[7])
        assertEquals(monsterSectionExpected9, result.keys.toList()[8])
        assertEquals(monsterSectionExpected10, result.keys.toList()[9])
        assertEquals(monsterSectionExpected11, result.keys.toList()[10])
        assertEquals(monsterSectionExpected12, result.keys.toList()[11])

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
            ),
            result[monsterSectionExpected3]
        )

        assertEquals(
            listOf(
                monsters[7] to monsters[8],
                monsters[9] to null
            ),
            result[monsterSectionExpected4]
        )

        assertEquals(
            listOf(
                monsters[13] to monsters[14],
            ),
            result[monsterSectionExpected5]
        )

        assertEquals(
            listOf(
                monsters[15] to null,
                monsters[16] to monsters[17],
                monsters[18] to null,
            ),
            result[monsterSectionExpected6]
        )

        assertEquals(
            listOf(
                monsters[10] to null,
            ),
            result[monsterSectionExpected7]
        )

        assertEquals(
            listOf(
                monsters[11] to monsters[12],
            ),
            result[monsterSectionExpected8]
        )

        assertEquals(
            listOf(
                monsters[19] to monsters[20],
            ),
            result[monsterSectionExpected9]
        )

        assertEquals(
            listOf(
                monsters[21] to null,
            ),
            result[monsterSectionExpected10]
        )

        assertEquals(
            listOf(
                monsters[23] to null,
            ),
            result[monsterSectionExpected11]
        )

        assertEquals(
            listOf(
                monsters[22] to null,
            ),
            result[monsterSectionExpected12]
        )
    }

    private fun createMonster(
        index: String,
        name: String,
        group: String? = null,
        isHorizontal: Boolean = true
    ): Monster {
        return Monster(
            preview = MonsterPreview(
                index = index,
                type = MonsterType.ABERRATION,
                challengeRating = 0.0f,
                name = name,
                imageData = MonsterImageData(
                    url = "",
                    backgroundColor = Color(light = "", dark = ""),
                    isHorizontal = isHorizontal
                ),
            ),
            subtype = null,
            group = group,
            subtitle = "",
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

    private fun MonstersBySection.toMonsters(): List<MonsterPreview> {
        val monsters = mutableListOf<MonsterPreview>()
        this.toList().map { it.second }.reduce { acc, list -> acc + list }.toList()
            .forEach { pair ->
                monsters.add(pair.first)
                pair.second?.let { monsters.add(it) }
            }
        return monsters
    }
}
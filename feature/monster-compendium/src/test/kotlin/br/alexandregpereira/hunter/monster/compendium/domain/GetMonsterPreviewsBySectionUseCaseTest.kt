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

import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterSection
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.sort.sortMonstersByNameAndGroup
import br.alexandregpereira.hunter.domain.sync.HandleSyncUseCase
import br.alexandregpereira.hunter.domain.sync.SyncUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterPreviewsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMonsterPreviewsBySectionUseCaseTest {

    private val syncUseCase: SyncUseCase = mockk()
    private val getMonstersUseCase: GetMonsterPreviewsUseCase = mockk()
    private val handleSyncUseCase: HandleSyncUseCase = mockk()
    private val useCase =
        GetMonsterPreviewsBySectionUseCase(syncUseCase, getMonstersUseCase, handleSyncUseCase)

    private fun createMonsters(): List<Monster> {
        return (0..23).map { index ->
            val name = when (index) {
                2 -> "Aztest$index"
                in 5..6 -> "Aztest$index"
                in 0..6 -> "A1test$index"
                in 7..11 -> "B1test$index"
                in 12..14 -> "C1test$index"
                in 15..18 -> "D1test$index"
                22 -> "Hztest$index"
                else -> "Hatest$index"
            }
            createMonster(
                index = index.toString(),
                name = name,
                group = if (index == 1 || index in 3..4) "Any" else if (index == 10) "Group1" else if (index in 11..12) "Group2" else if (index in 19..20) "Hags" else if (index == 23) "Hxgs1" else null,
                isHorizontal = index != 0 && index != 7
            )
        }
    }

    @Test
    fun invoke() = runBlocking {
        // Given
        val monsters = createMonsters().map { it.preview }
        every { handleSyncUseCase() } returns flowOf(Unit)
        every { syncUseCase() } returns flowOf(Unit)
        every { getMonstersUseCase() } returns flowOf(createMonsters().sortMonstersByNameAndGroup())

        // When
        val result = useCase().single()

        // Then
        val monsterSectionExpected1 = MonsterSection(title = "A", id = "A0", isHeader = true)
        val monsterSectionExpected2 = MonsterSection(title = "Any", parentTitle = null)
        val monsterSectionExpected3 = MonsterSection(title = "A", id = "A1", isHeader = false)
        val monsterSectionExpected4 = MonsterSection(title = "B", id = "B0", isHeader = true)
        val monsterSectionExpected5 = MonsterSection(title = "C", id = "C0", isHeader = true)
        val monsterSectionExpected6 = MonsterSection(title = "D", id = "D0", isHeader = true)
        val monsterSectionExpected7 = MonsterSection(title = "Group1", parentTitle = "G")
        val monsterSectionExpected8 = MonsterSection(title = "Group2", parentTitle = null)
        val monsterSectionExpected9 = MonsterSection(title = "Hags", parentTitle = "H")
        val monsterSectionExpected10 = MonsterSection(title = "H", id = "H1", isHeader = false)
        val monsterSectionExpected11 = MonsterSection(title = "Hxgs1", parentTitle = null)
        val monsterSectionExpected12 = MonsterSection(title = "H", id = "H2", isHeader = false)
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
                monsters[0].changeIsHorizontalImage(isHorizontal = true),
            ),
            result[monsterSectionExpected1]
        )

        assertEquals(
            listOf(
                monsters[1].changeIsHorizontalImage(isHorizontal = true),
                monsters[3].changeIsHorizontalImage(isHorizontal = false),
                monsters[4].changeIsHorizontalImage(isHorizontal = false)
            ),
            result[monsterSectionExpected2]
        )

        assertEquals(
            listOf(
                monsters[2].changeIsHorizontalImage(isHorizontal = true),
                monsters[5].changeIsHorizontalImage(isHorizontal = false),
                monsters[6].changeIsHorizontalImage(isHorizontal = false),
            ),
            result[monsterSectionExpected3]
        )

        assertEquals(
            listOf(
                monsters[7].changeIsHorizontalImage(isHorizontal = false),
                monsters[8].changeIsHorizontalImage(isHorizontal = false),
                monsters[9].changeIsHorizontalImage(isHorizontal = true)
            ),
            result[monsterSectionExpected4]
        )

        assertEquals(
            listOf(
                monsters[13].changeIsHorizontalImage(isHorizontal = false),
                monsters[14].changeIsHorizontalImage(isHorizontal = false),
            ),
            result[monsterSectionExpected5]
        )

        assertEquals(
            listOf(
                monsters[15].changeIsHorizontalImage(isHorizontal = true),
                monsters[16].changeIsHorizontalImage(isHorizontal = false),
                monsters[17].changeIsHorizontalImage(isHorizontal = false),
                monsters[18].changeIsHorizontalImage(isHorizontal = true),
            ),
            result[monsterSectionExpected6]
        )

        assertEquals(
            listOf(
                monsters[10].changeIsHorizontalImage(isHorizontal = true),
            ),
            result[monsterSectionExpected7]
        )

        assertEquals(
            listOf(
                monsters[11].changeIsHorizontalImage(isHorizontal = false),
                monsters[12].changeIsHorizontalImage(isHorizontal = false),
            ),
            result[monsterSectionExpected8]
        )

        assertEquals(
            listOf(
                monsters[19].changeIsHorizontalImage(isHorizontal = false),
                monsters[20].changeIsHorizontalImage(isHorizontal = false),
            ),
            result[monsterSectionExpected9]
        )

        assertEquals(
            listOf(
                monsters[21].changeIsHorizontalImage(isHorizontal = true),
            ),
            result[monsterSectionExpected10]
        )

        assertEquals(
            listOf(
                monsters[23].changeIsHorizontalImage(isHorizontal = true),
            ),
            result[monsterSectionExpected11]
        )

        assertEquals(
            listOf(
                monsters[22].changeIsHorizontalImage(isHorizontal = true),
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
            actions = listOf(),
            sourceName = ""
        )
    }

    private fun MonstersBySection.toMonsters(): List<MonsterPreview> {
        val monsters = mutableListOf<MonsterPreview>()
        this.toList().map { it.second }.reduce { acc, list -> acc + list }.toList()
            .forEach { monster ->
                monsters.add(monster)
            }
        return monsters
    }

    private fun MonsterPreview.changeIsHorizontalImage(isHorizontal: Boolean): MonsterPreview {
        return copy(imageData = imageData.copy(isHorizontal = isHorizontal))
    }
}

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

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SaveMonstersUseCaseTest {

    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase = mockk()
    private val monsterRepository: MonsterRepository = mockk()
    private val measurementUnitRepository: MeasurementUnitRepository = mockk()

    private val useCase = SaveMonstersUseCase(
        getMeasurementUnitUseCase,
        monsterRepository,
        measurementUnitRepository,
    )

    @Test
    fun `invoke Should change from feet to meters`() = runBlocking {
        // Given
        every { getMeasurementUnitUseCase() } returns flowOf(MeasurementUnit.METER)
        every { monsterRepository.saveMonsters(any()) } returns flowOf(Unit)
        every {
            measurementUnitRepository.getPreviousMeasurementUnit()
        } returns flowOf(MeasurementUnit.FEET)
        val monsters = createFeetMonsters()

        // When
        useCase(monsters).single()

        // Then
        val speedExpected = Speed(
            hover = false,
            values = listOf(
                SpeedValue(type = SpeedType.WALK, valueFormatted = "15m")
            )
        )
        val sensesExpected = "9m/9 meters/9m"
        val languagesExpected = "something, telepathy 36.5m"
        val abilityDescriptionExpected = AbilityDescription(
            name = "",
            description = "ASDAS sada 1.5m//asdqweqweqw 9m-asd-6m 3m"
        )
        val actionExpected = Action(
            damageDices = listOf(),
            attackBonus = null,
            abilityDescriptionExpected
        )

        verify {
            monsterRepository.saveMonsters(
                eq(
                    monsters.map {
                        val expectedImageUrl = it.imageData.url
                        it.copy(
                            speed = speedExpected,
                            senses = listOf(sensesExpected),
                            languages = languagesExpected,
                            specialAbilities = listOf(abilityDescriptionExpected),
                            actions = listOf(actionExpected),
                            imageData = it.imageData.copy(url = expectedImageUrl)
                        )
                    }
                )
            )
        }
    }

    @Test
    fun `invoke Should change from meters to feet`() = runBlocking {
        // Given
        every { getMeasurementUnitUseCase() } returns flowOf(MeasurementUnit.FEET)
        every { monsterRepository.saveMonsters(any()) } returns flowOf(Unit)
        every {
            measurementUnitRepository.getPreviousMeasurementUnit()
        } returns flowOf(MeasurementUnit.METER)
        val monsters = createMetersMonsters()

        // When
        useCase(monsters).single()

        // Then
        val speedExpected = Speed(
            hover = false,
            values = listOf(
                SpeedValue(type = SpeedType.WALK, valueFormatted = "50 ft.")
            )
        )
        val sensesExpected = "30 ft./30 feet/30 ft."
        val languagesExpected = "something, telepathy 120 ft."
        val abilityDescriptionExpected = AbilityDescription(
            name = "",
            description = "ASDAS sada 5 ft.//asdqweqweqw 30 ft.-asd-20 ft. 10 ft."
        )
        val actionExpected = Action(
            damageDices = listOf(),
            attackBonus = null,
            abilityDescriptionExpected
        )

        verify {
            monsterRepository.saveMonsters(
                eq(
                    monsters.map {
                        val expectedImageUrl = it.imageData.url
                        it.copy(
                            speed = speedExpected,
                            senses = listOf(sensesExpected),
                            languages = languagesExpected,
                            specialAbilities = listOf(abilityDescriptionExpected),
                            actions = listOf(actionExpected),
                            imageData = it.imageData.copy(url = expectedImageUrl)
                        )
                    }
                )
            )
        }
    }

    private fun createFeetMonsters(): List<Monster> {
        return listOf(
            Monster(
                index = "",
                name = "",
                type = MonsterType.CELESTIAL,
                challengeRating = 0.0f,
                imageData = MonsterImageData(
                    url = "something.png",
                    backgroundColor = Color(light = "", dark = ""),
                    isHorizontal = false
                ),
                subtype = null,
                group = null,
                subtitle = "",
                size = "",
                alignment = "",
                stats = Stats(
                    armorClass = 0,
                    hitPoints = 0,
                    hitDice = ""
                ),
                speed = Speed(
                    hover = false,
                    values = listOf(
                        SpeedValue(type = SpeedType.WALK, valueFormatted = "50 ft.")
                    )
                ),
                senses = listOf(
                    "30 ft./30 feet/30 ft ."
                ),
                languages = "something, telepathy 120 ft.",
                specialAbilities = listOf(
                    AbilityDescription(
                        name = "",
                        description = "ASDAS sada 5 ft.//asdqweqweqw 30 ft.-asd-20 ft. 10ft."
                    )
                ),
                actions = listOf(
                    Action(
                        damageDices = listOf(),
                        attackBonus = null,
                        AbilityDescription(
                            name = "",
                            description = "ASDAS sada 5 ft.//asdqweqweqw 30 ft.-asd-20 ft. 10ft."
                        )
                    )
                ),
                sourceName = ""
            )
        )
    }

    private fun createMetersMonsters(): List<Monster> {
        return listOf(
            Monster(
                index = "",
                name = "",
                type = MonsterType.CELESTIAL,
                challengeRating = 0.0f,
                imageData = MonsterImageData(
                    url = "something.png",
                    backgroundColor = Color(light = "", dark = ""),
                    isHorizontal = false
                ),
                subtype = null,
                group = null,
                subtitle = "",
                size = "",
                alignment = "",
                stats = Stats(
                    armorClass = 0,
                    hitPoints = 0,
                    hitDice = ""
                ),
                speed = Speed(
                    hover = false,
                    values = listOf(
                        SpeedValue(type = SpeedType.WALK, valueFormatted = "15m")
                    )
                ),
                senses = listOf(
                    "9m/9 meters/9m"
                ),
                languages = "something, telepathy 36.5m",
                specialAbilities = listOf(
                    AbilityDescription(
                        name = "",
                        description = "ASDAS sada 1.5m//asdqweqweqw 9m-asd-6m 3m"
                    )
                ),
                actions = listOf(
                    Action(
                        damageDices = listOf(),
                        attackBonus = null,
                        AbilityDescription(
                            name = "",
                            description = "ASDAS sada 1.5m//asdqweqweqw 9m-asd-6m 3m"
                        )
                    )
                ),
                sourceName = ""
            )
        )
    }
}
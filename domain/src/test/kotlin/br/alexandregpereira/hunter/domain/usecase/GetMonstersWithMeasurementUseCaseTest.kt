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

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.Color
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImageData
import br.alexandregpereira.hunter.domain.model.MonsterPreview
import br.alexandregpereira.hunter.domain.model.MonsterType
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.model.SpeedType
import br.alexandregpereira.hunter.domain.model.SpeedValue
import br.alexandregpereira.hunter.domain.model.Stats
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMonstersWithMeasurementUseCaseTest {

    private val repository: MeasurementUnitRepository = mockk()
    private val getMonstersUseCase: GetMonstersUseCase = mockk()
    private val useCase = GetMonstersWithMeasurementUseCase(repository, getMonstersUseCase)

    @Test
    fun invoke() = runBlocking {
        // Given
        every { repository.getMeasurementUnit() } returns flowOf(MeasurementUnit.METER)
        every { getMonstersUseCase() } returns flowOf(createMonsters())

        // When
        val result = useCase().single()

        // Then
        val speedExpected = Speed(
            hover = false,
            values = listOf(
                SpeedValue(type = SpeedType.WALK, valueFormatted = "15m")
            )
        )
        val abilityDescriptionExpected = AbilityDescription(
            name = "",
            description = "ASDAS sada 1.5m\nasdqweqweqw 9m-asd-6m 10ft."
        )
        val actionExpected = Action(
            damageDices = listOf(),
            attackBonus = null,
            abilityDescriptionExpected
        )
        result.first().run {
            assertEquals(speedExpected, speed)
            assertEquals(abilityDescriptionExpected, specialAbilities.first())
            assertEquals(actionExpected, actions.first())
        }
    }

    private fun createMonsters(): List<Monster> {
        return listOf(
            Monster(
                preview = MonsterPreview(
                    index = "",
                    name = "",
                    type = MonsterType.CELESTIAL,
                    challengeRating = 0.0f,
                    imageData = MonsterImageData(
                        url = "",
                        backgroundColor = Color(light = "", dark = ""),
                        isHorizontal = false
                    )
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
                    "30 ft."
                ),
                languages = "",
                specialAbilities = listOf(
                    AbilityDescription(
                        name = "",
                        description = "ASDAS sada 5 ft.\nasdqweqweqw 30 ft.-asd-20 ft. 10ft."
                    )
                ),
                actions = listOf(
                    Action(
                        damageDices = listOf(),
                        attackBonus = null,
                        AbilityDescription(
                            name = "",
                            description = "ASDAS sada 5 ft.\nasdqweqweqw 30 ft.-asd-20 ft. 10ft."
                        )
                    )
                )
            )
        )
    }
}
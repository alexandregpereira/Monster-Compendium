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
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import java.math.RoundingMode
import kotlin.math.roundToInt

class GetMonstersWithMeasurementUseCase(
    private val repository: MeasurementUnitRepository,
    private val getMonstersUseCase: GetMonstersUseCase
) {

    operator fun invoke(): Flow<List<Monster>> {
        return repository.getMeasurementUnit().zip(
            getMonstersUseCase()
        ) { unit: MeasurementUnit, monsters: List<Monster> ->
            monsters.changeMeasurementUnit(unit)
        }
    }

    private fun List<Monster>.changeMeasurementUnit(unit: MeasurementUnit): List<Monster> {
        if (MeasurementUnit.FEET == unit) return this

        return this.map { monster ->
            monster.changeMeasurementUnit(unit)
        }
    }

    private fun Monster.changeMeasurementUnit(unit: MeasurementUnit): Monster {
        return this.copy(
            speed = this.speed.changeMeasurementUnit(unit),
            senses = this.senses.changeMeasurementUnit(unit),
            specialAbilities = this.specialAbilities.changeMeasurementUnit(unit),
            actions = this.actions.changeMeasurementUnit(unit)
        )
    }

    private fun Speed.changeMeasurementUnit(unit: MeasurementUnit): Speed {
        return this.copy(
            values = this.values.map { speedValue ->
                speedValue.copy(
                    valueFormatted = speedValue.valueFormatted
                        .changeMeasurementUnit(unit)
                )
            }
        )
    }

    @JvmName("changeMeasurementUnitAbilityDescription")
    private fun List<AbilityDescription>.changeMeasurementUnit(
        unit: MeasurementUnit
    ): List<AbilityDescription> {
        return this.map {
            it.changeMeasurementUnit(unit)
        }
    }

    private fun AbilityDescription.changeMeasurementUnit(
        unit: MeasurementUnit
    ): AbilityDescription {
        return this.copy(
            description = this.description.changeMeasurementUnit(unit)
        )
    }

    @JvmName("changeMeasurementUnitAction")
    private fun List<Action>.changeMeasurementUnit(unit: MeasurementUnit): List<Action> {
        return this.map {
            it.copy(
                abilityDescription = it.abilityDescription.changeMeasurementUnit(unit)
            )
        }
    }

    @JvmName("changeMeasurementUnitString")
    private fun List<String>.changeMeasurementUnit(unit: MeasurementUnit): List<String> {
        return this.map { it.changeMeasurementUnit(unit) }
    }

    private fun String.changeMeasurementUnit(unit: MeasurementUnit): String {
        val previousUnit = MeasurementUnit.FEET
        val regex = "\\d+ ${previousUnit.value}".toRegex()
        val matches = regex.findAll(this).map { it.groupValues.first() }
        var nextText = this

        matches.forEach { match ->
            val value = match.split(" ").first()
            val valueFormatted = when (unit) {
                MeasurementUnit.FEET -> "${value.toDouble().toFeet()} ${unit.value}"
                MeasurementUnit.METER -> "${value.toInt().toMeters().formatMeters()}${unit.value}"
            }
            nextText = nextText.replace(match, valueFormatted)
        }

        return nextText
    }

    private fun Int.toMeters(): Double = this / 3.281

    private fun Double.toFeet(): Int = (this * 3.281).roundToInt()

    private fun Double.formatMeters(): String {
        val value = this.toBigDecimal().setScale(1, RoundingMode.DOWN)
        val intValue = value.toInt()
        val decimalValue = (value - intValue.toBigDecimal()).toDouble()

        return if (decimalValue == 0.5) {
            value.toString()
        }
        else {
            intValue.toString()
        }
    }
}
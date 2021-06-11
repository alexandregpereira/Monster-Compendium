/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
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

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.zip
import java.math.RoundingMode

class SaveMonstersUseCase internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val monsterRepository: MonsterRepository,
    private val measurementUnitRepository: MeasurementUnitRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(monsters: List<Monster>): Flow<Unit> {
        return getMeasurementUnitUseCase()
            .zip(measurementUnitRepository.getPreviousMeasurementUnit()) { unit, previousUnit ->
                monsters.changeMeasurementUnit(previousUnit, unit)
            }.flatMapLatest {
                monsterRepository.saveMonsters(monsters = it)
            }
    }

    private fun List<Monster>.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<Monster> {
        if (previousUnit == unit) return this

        return this.map { monster ->
            monster.changeMeasurementUnit(previousUnit, unit)
        }
    }

    private fun Monster.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): Monster {
        return this.copy(
            speed = this.speed.changeMeasurementUnit(previousUnit, unit),
            senses = this.senses.changeMeasurementUnit(previousUnit, unit),
            languages = this.languages.changeMeasurementUnit(previousUnit, unit),
            specialAbilities = this.specialAbilities.changeMeasurementUnit(previousUnit, unit),
            actions = this.actions.changeMeasurementUnit(previousUnit, unit)
        )
    }

    private fun Speed.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): Speed {
        return this.copy(
            values = this.values.map { speedValue ->
                speedValue.copy(
                    valueFormatted = speedValue.valueFormatted
                        .changeMeasurementUnit(previousUnit, unit)
                )
            }
        )
    }

    @JvmName("changeMeasurementUnitAbilityDescription")
    private fun List<AbilityDescription>.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<AbilityDescription> {
        return this.map {
            it.changeMeasurementUnit(previousUnit, unit)
        }
    }

    private fun AbilityDescription.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): AbilityDescription {
        return this.copy(
            description = this.description.changeMeasurementUnit(previousUnit, unit)
        )
    }

    @JvmName("changeMeasurementUnitAction")
    private fun List<Action>.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<Action> {
        return this.map {
            it.copy(
                abilityDescription = it.abilityDescription.changeMeasurementUnit(previousUnit, unit)
            )
        }
    }

    @JvmName("changeMeasurementUnitString")
    private fun List<String>.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<String> {
        return this.map { it.changeMeasurementUnit(previousUnit, unit) }
    }

    private fun String.changeMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): String {
        val regex = previousUnit.possibilities.joinToString(separator = "|") { value ->
            val valueFormatted = value.replace(".", "[.]")
            "\\d+$valueFormatted|\\d+(\\.\\d{1,2})+$valueFormatted"
        }.toRegex()

        val matches = regex.findAll(this).map { it.groupValues.first() }
        var nextText = this

        matches.forEach { match ->
            val unitValue = getUnitValue(match, previousUnit, unit)
            val value = previousUnit.getValue(match)
            val valueFormatted = when (unit) {
                MeasurementUnit.FEET -> "${value.toDouble().toFeet()}$unitValue"
                MeasurementUnit.METER -> "${value.toInt().toMeters().formatMeters()}$unitValue"
            }
            nextText = nextText.replace(match, valueFormatted)
        }

        return nextText
    }

    private fun MeasurementUnit.getValue(matchString: String): String {
        return this.possibilities.find {
            matchString.endsWith(it)
        }?.let {
            matchString.removeSuffix(it)
        } ?: throw IllegalAccessException("Value not found")
    }

    private fun getUnitValue(
        matchString: String,
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): String {
        return previousUnit.values.indexOfFirst {
            matchString.replace("ft .", "ft.").endsWith(it)
        }.takeIf { it >= 0 }?.let { i ->
            unit.values[i]
        } ?: unit.values.first()
    }

    private fun Int.toMeters(): Double = this / 3.281

    private fun Double.toFeet(): Int = (this * 3.281)
        .toBigDecimal()
        .setScale(0, RoundingMode.UP)
        .toInt()

    private fun Double.formatMeters(): String {
        val value = this.toBigDecimal().setScale(1, RoundingMode.DOWN)
        val intValue = value.toInt()
        val decimalValue = (value - intValue.toBigDecimal()).toDouble()

        return if (decimalValue == 0.5) {
            value.toString()
        } else {
            intValue.toString()
        }
    }
}

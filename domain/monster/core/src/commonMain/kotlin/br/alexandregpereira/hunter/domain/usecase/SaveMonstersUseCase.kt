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

import br.alexandregpereira.hunter.domain.model.AbilityDescription
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.Speed
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.repository.MonsterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.zip

interface SaveMonstersUseCase {
    operator fun invoke(monsters: List<Monster>, isSync: Boolean = false): Flow<Unit>
}

fun SaveMonstersUseCase(
    getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    monsterRepository: MonsterRepository,
    measurementUnitRepository: MeasurementUnitRepository,
    monsterImageRepository: MonsterImageRepository,
): SaveMonstersUseCase = SaveMonstersUseCaseImpl(
    getMeasurementUnitUseCase,
    monsterRepository,
    measurementUnitRepository,
    monsterImageRepository,
)

private class SaveMonstersUseCaseImpl(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val monsterRepository: MonsterRepository,
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val monsterImageRepository: MonsterImageRepository,
) : SaveMonstersUseCase {

    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return getMeasurementUnitUseCase()
            .zip(measurementUnitRepository.getPreviousMeasurementUnit()) { unit, previousUnit ->
                monsters.changeMonstersMeasurementUnit(previousUnit, unit)
            }
            .flatMapLatest {
                saveMonsters(monsters = it, isSync)
            }
    }

    private fun saveMonsters(monsters: List<Monster>, isSync: Boolean): Flow<Unit> {
        return flow {
            coroutineScope {
                val saveMonstersDeferred = async {
                    monsterRepository.saveMonsters(monsters = monsters, isSync).collect()
                }
                val saveMonsterImagesDeferred = async {
                    if (isSync.not()) {
                        monsterImageRepository.saveMonsterImages(
                            monsterImages = monsters.toMonsterImages()
                        )
                    }
                }

                saveMonstersDeferred.await()
                saveMonsterImagesDeferred.await()

                emit(Unit)
            }
        }
    }

    private fun List<Monster>.toMonsterImages(): List<MonsterImage> {
        return map {
            MonsterImage(
                monsterIndex = it.index,
                backgroundColor = it.imageData.backgroundColor,
                isHorizontalImage = it.imageData.isHorizontal,
                imageUrl = it.imageData.url,
                contentScale = it.imageData.contentScale,
            )
        }
    }

    private fun List<Monster>.changeMonstersMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<Monster> {
        if (previousUnit == unit) return this

        return this.map { monster ->
            monster.changeMonsterMeasurementUnit(previousUnit, unit)
        }
    }

    private fun Monster.changeMonsterMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): Monster {
        return this.copy(
            speed = this.speed.changeSpeedMeasurementUnit(previousUnit, unit),
            senses = this.senses.changeStringsMeasurementUnit(previousUnit, unit),
            languages = this.languages.changeMeasurementUnit(previousUnit, unit),
            specialAbilities = this.specialAbilities.changeAbilitiesMeasurementUnit(previousUnit, unit),
            actions = this.actions.changeActionMeasurementUnit(previousUnit, unit)
        )
    }

    private fun Speed.changeSpeedMeasurementUnit(
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

    private fun List<AbilityDescription>.changeAbilitiesMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<AbilityDescription> {
        return this.map {
            it.changeAbilityMeasurementUnit(previousUnit, unit)
        }
    }

    private fun AbilityDescription.changeAbilityMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): AbilityDescription {
        return this.copy(
            description = this.description.changeMeasurementUnit(previousUnit, unit)
        )
    }

    private fun List<Action>.changeActionMeasurementUnit(
        previousUnit: MeasurementUnit,
        unit: MeasurementUnit
    ): List<Action> {
        return this.map {
            it.copy(
                abilityDescription = it.abilityDescription.changeAbilityMeasurementUnit(previousUnit, unit)
            )
        }
    }

    private fun List<String>.changeStringsMeasurementUnit(
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
        } ?: throw RuntimeException("Value not found")
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

    private fun Double.toFeet(): Int = (this * 3.281).run {
        val value = this
        val intValue = this.toInt()
        (value - intValue).let {
            if (it < 0.1) intValue else intValue + 1
        }
    }

    private fun Double.formatMeters(): String {
        val value = this
        val intValue = this.toInt()
        return (value - intValue).let {
            if (it < 0.6) intValue else intValue + 1
        }.toString()
    }
}

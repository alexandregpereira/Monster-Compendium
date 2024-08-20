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

package br.alexandregpereira.hunter.data.monster.preferences

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import br.alexandregpereira.hunter.domain.settings.SettingsRepository
import br.alexandregpereira.hunter.domain.settings.getValue
import br.alexandregpereira.hunter.domain.settings.saveValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MonsterPreferencesRepository(
    private val settingsRepository: SettingsRepository
) : CompendiumRepository, MeasurementUnitRepository {

    override fun getLastCompendiumScrollItemPosition(): Flow<Int> {
        return settingsRepository.getValue(COMPENDIUM_SCROLL_ITEM_POSITION_KEY)
    }

    override fun saveCompendiumScrollItemPosition(position: Int): Flow<Unit> {
        return settingsRepository.saveValue(COMPENDIUM_SCROLL_ITEM_POSITION_KEY, position)
    }

    override fun saveMeasurementUnit(measurementUnit: MeasurementUnit): Flow<Unit> {
        return saveMeasurementUnit(MEASUREMENT_UNIT_KEY, measurementUnit)
    }

    override fun getMeasurementUnit(): Flow<MeasurementUnit> {
        return getMeasurementUnit(MEASUREMENT_UNIT_KEY)
    }

    override fun savePreviousMeasurementUnit(measurementUnit: MeasurementUnit): Flow<Unit> {
        return saveMeasurementUnit(PREVIOUS_MEASUREMENT_UNIT_KEY, measurementUnit)
    }

    override fun getPreviousMeasurementUnit(): Flow<MeasurementUnit> {
        return getMeasurementUnit(PREVIOUS_MEASUREMENT_UNIT_KEY)
    }

    private fun saveMeasurementUnit(key: String, measurementUnit: MeasurementUnit): Flow<Unit> {
        return settingsRepository.saveString(key, measurementUnit.name)
    }

    private fun getMeasurementUnit(key: String): Flow<MeasurementUnit> {
        return settingsRepository.getValue(
            key,
            defaultValue = MeasurementUnit.FEET.name
        ).map {
            MeasurementUnit.valueOf(it)
        }
    }
}

private const val COMPENDIUM_SCROLL_ITEM_POSITION_KEY = "COMPENDIUM_SCROLL_ITEM_POSITION_KEY"
private const val MEASUREMENT_UNIT_KEY = "MEASUREMENT_UNIT_KEY"
private const val PREVIOUS_MEASUREMENT_UNIT_KEY = "PREVIOUS_MEASUREMENT_UNIT_KEY"

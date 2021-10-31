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

package br.alexandregpereira.hunter.data.preferences

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.repository.CompendiumRepository
import br.alexandregpereira.hunter.domain.repository.MeasurementUnitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PreferencesRepository @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : CompendiumRepository, MeasurementUnitRepository {

    override fun getLastCompendiumScrollItemPosition(): Flow<Int> {
        return preferencesDataSource.getInt(COMPENDIUM_SCROLL_ITEM_POSITION_KEY)
    }

    override fun saveCompendiumScrollItemPosition(position: Int): Flow<Unit> {
        return preferencesDataSource.save(COMPENDIUM_SCROLL_ITEM_POSITION_KEY, position)
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
        return preferencesDataSource.save(key, measurementUnit.name)
    }

    private fun getMeasurementUnit(key: String): Flow<MeasurementUnit> {
        return preferencesDataSource.getString(
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

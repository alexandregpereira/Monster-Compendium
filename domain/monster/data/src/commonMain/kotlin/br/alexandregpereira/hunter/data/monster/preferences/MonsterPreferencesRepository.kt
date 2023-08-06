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

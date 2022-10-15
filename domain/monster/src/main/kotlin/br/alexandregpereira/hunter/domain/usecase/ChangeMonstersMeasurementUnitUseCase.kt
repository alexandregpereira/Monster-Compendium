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

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class ChangeMonstersMeasurementUnitUseCase @Inject internal constructor(
    private val saveMeasurementUnitUseCase: SaveMeasurementUnitUseCase,
    private val saveMonstersUseCase: SaveMonstersUseCase,
    private val getMonstersUseCase: GetMonstersUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(measurementUnit: MeasurementUnit): Flow<Unit> {
        return getMonstersUseCase()
            .zip(saveMeasurementUnitUseCase(measurementUnit)) { monsters, _ ->
                monsters
            }.flatMapLatest { monsters ->
                saveMonstersUseCase(monsters)
            }
    }
}
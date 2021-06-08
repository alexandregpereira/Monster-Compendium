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

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.zip

class ChangeMonstersMeasurementUnitUseCase internal constructor(
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
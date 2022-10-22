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

package br.alexandregpereira.hunter.detail.domain

import br.alexandregpereira.hunter.detail.domain.model.MonsterDetail
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersByIdsUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

class GetMonsterDetailUseCase @Inject internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
    private val getMonsterUseCase: GetMonsterUseCase,
    private val getMonstersByIds: GetMonstersByIdsUseCase,
) {

    operator fun invoke(
        index: String,
        indexes: List<String>,
    ): Flow<MonsterDetail> {
        return getMonsters(index, indexes)
            .zip(getMeasurementUnitUseCase()) { monsters, measurementUnit ->
                val monster = monsters.find { monster -> monster.index == index }
                    ?: throw IllegalAccessError("Monster not found")

                MonsterDetail(
                    monsterIndexSelected = monsters.indexOf(monster),
                    measurementUnit = measurementUnit,
                    monsters = monsters
                )
            }
    }

    private fun getMonsters(
        index: String,
        indexes: List<String>,
    ): Flow<List<Monster>> {
        return if (indexes.isEmpty()) {
            getMonstersUseCase()
        } else if (indexes.size == 1){
            getMonsterUseCase(index).map { listOf(it) }
        } else {
            getMonstersByIds(indexes)
        }
    }
}

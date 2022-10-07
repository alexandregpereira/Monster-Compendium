/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.detail.domain

import br.alexandregpereira.domain.folder.GetMonstersByFolderUseCase
import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import br.alexandregpereira.hunter.domain.usecase.GetMeasurementUnitUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonsterUseCase
import br.alexandregpereira.hunter.domain.usecase.GetMonstersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

typealias MonsterDetail = Triple<Int, List<Monster>, MeasurementUnit>

class GetMonsterDetailUseCase @Inject internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
    private val getMonsterUseCase: GetMonsterUseCase,
    private val getMonstersByFolder: GetMonstersByFolderUseCase
) {

    operator fun invoke(
        index: String,
        isSingleMonster: Boolean,
        folderName: String
    ): Flow<MonsterDetail> {
        return getMonsters(index, isSingleMonster, folderName)
            .zip(getMeasurementUnitUseCase()) { monsters, measurementUnit ->
                val monster = monsters.find { monster -> monster.index == index }
                    ?: throw IllegalAccessError("Monster not found")

                Triple(monsters.indexOf(monster), monsters, measurementUnit)
            }
    }

    private fun getMonsters(
        index: String,
        isSingleMonster: Boolean,
        folderName: String
    ): Flow<List<Monster>> {
        return if (isSingleMonster) {
            getMonsterUseCase(index).map { listOf(it) }
        } else if (folderName.isNotBlank()) {
            getMonstersByFolder(folderName)
        } else {
            getMonstersUseCase()
        }
    }
}

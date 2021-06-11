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

package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.dndapi.data.model.Description
import br.alexandregpereira.hunter.dndapi.data.DamageTypeApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private val api: DamageTypeApi = retrofit.create(DamageTypeApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = start {
    val response = api.getDamageTypes()

    val skills = response.results.asFlow()
        .flatMapMerge {
            getSkill(it.index)
        }
        .toList()
        .sortedBy { it.index }

    saveJsonFile(skills, DAMAGE_TYPE_JSON_FILE_NAME)
}

@ExperimentalSerializationApi
private suspend fun getSkill(index: String): Flow<Description> = flow {
    println("Damage Type: $index")
    emit(api.getDamageType(index))
}

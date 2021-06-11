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

import br.alexandregpereira.hunter.dndapi.data.ConditionApi
import br.alexandregpereira.hunter.dndapi.data.model.Description
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private val api: ConditionApi = retrofit.create(ConditionApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = start {
    val response = api.getConditions()

    val result = response.results.asFlow()
        .flatMapMerge {
            getCondition(it.index)
        }
        .toList()
        .sortedBy { it.index }

    saveJsonFile(result, CONDITION_JSON_FILE_NAME)
}

@ExperimentalSerializationApi
private suspend fun getCondition(index: String): Flow<Description> = flow {
    println("Condition: $index")
    emit(api.getCondition(index))
}

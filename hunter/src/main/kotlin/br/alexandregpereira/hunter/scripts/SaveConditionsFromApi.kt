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

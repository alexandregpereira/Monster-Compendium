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

import br.alexandregpereira.hunter.dndapi.data.Skill
import br.alexandregpereira.hunter.dndapi.data.SkillApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
private val skillApi: SkillApi = retrofit.create(SkillApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = start {
    val skillResponse = skillApi.getSkills()

    val skills = skillResponse.results.asFlow()
        .flatMapMerge {
            getSkill(it.index)
        }
        .toList()
        .sortedBy { it.index }

    saveJsonFile(skills, SKILL_JSON_FILE_NAME)
}

@ExperimentalSerializationApi
private suspend fun getSkill(index: String): Flow<Skill> = flow {
    println("Skill: $index")
    emit(skillApi.getSkill(index))
}

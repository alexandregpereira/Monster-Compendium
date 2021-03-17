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

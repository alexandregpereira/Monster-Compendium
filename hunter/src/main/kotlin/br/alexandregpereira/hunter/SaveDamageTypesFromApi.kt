package br.alexandregpereira.hunter

import br.alexandregpereira.hunter.dndapi.data.DamageType
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
private suspend fun getSkill(index: String): Flow<DamageType> = flow {
    println("Damage Type: $index")
    emit(api.getDamageType(index))
}

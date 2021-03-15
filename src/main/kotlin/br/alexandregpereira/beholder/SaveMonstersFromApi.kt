package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.Monster
import br.alexandregpereira.beholder.dndapi.data.MonsterApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
val monsterApi: MonsterApi = retrofit.create(MonsterApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = start {
    val monsterResponse = monsterApi.getMonsters()

    val monsters = monsterResponse.results.asFlow()
        .flatMapMerge {
            getMonsterFlow(it.index)
        }.toList().sortedBy { it.name }

    saveJsonFile(monsters, JSON_FILE_NAME)
}

@ExperimentalSerializationApi
private suspend fun getMonsterFlow(index: String): Flow<Monster> = flow {
    emit(getMonster(index))
}

@ExperimentalSerializationApi
private suspend fun getMonster(index: String): Monster {
    println("Monster: $index")
    return monsterApi.getMonster(index)
}

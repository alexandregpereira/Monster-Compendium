package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.Monster
import br.alexandregpereira.beholder.dndapi.data.MonsterApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Retrofit

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.dnd5eapi.co/api/")
    .addConverterFactory(json.asConverterFactory(contentType))
    .build()

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
//    getMonster("ankheg")
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

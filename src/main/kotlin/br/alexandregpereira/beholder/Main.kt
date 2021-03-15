package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.Monster
import br.alexandregpereira.beholder.dndapi.data.MonsterApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import java.io.File

private const val JSON_FILE_NAME = "json/dndapi-monsters.json"
private const val GITHUB_IMAGE_HOST =
    "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/"

val contentType: MediaType = MediaType.get("application/json")
val json = Json { ignoreUnknownKeys = true }

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.dnd5eapi.co/api/")
    .addConverterFactory(json.asConverterFactory(contentType))
    .build()

@ExperimentalSerializationApi
val monsterApi: MonsterApi = retrofit.create(MonsterApi::class.java)

@FlowPreview
@ExperimentalSerializationApi
suspend fun main() = coroutineScope {
    val startTime = System.currentTimeMillis()
    println("Started\n")
    val monsterResponse = monsterApi.getMonsters()

    val monsters = monsterResponse.results.asFlow()
        .flatMapMerge {
            getMonsterFlow(it.index)
        }.toList().sortedBy { it.name }

    println("\nSaving")
    val json = Json.encodeToString(monsters)
    saveJsonFile(json)

//    getMonster("swarm-of-insects")
    println("\nFinished in ${System.currentTimeMillis() - startTime} ms")
}

fun saveJsonFile(json: String) {
    File(JSON_FILE_NAME).writeText(json)
    println(json)
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

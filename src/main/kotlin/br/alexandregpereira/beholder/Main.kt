package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.MonsterApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

private const val GITHUB_IMAGE_HOST = "https://raw.githubusercontent.com/alexandregpereira/dnd-monster-manual/main/images/"

val contentType: MediaType = MediaType.get("application/json")

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.dnd5eapi.co/api/")
    .addConverterFactory(Json {  ignoreUnknownKeys = true }.asConverterFactory(contentType))
    .build()

@ExperimentalSerializationApi
val service: MonsterApi = retrofit.create(MonsterApi::class.java)

@ExperimentalSerializationApi
suspend fun main() = coroutineScope {
    val monster = service.getMonster("minotaur")
    println(monster)
}

suspend fun saveJsonFile() {

}

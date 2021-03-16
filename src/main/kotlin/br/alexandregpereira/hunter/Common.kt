package br.alexandregpereira.hunter

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import java.io.File

const val JSON_FILE_NAME = "json/dndapi-monsters.json"
const val SKILL_JSON_FILE_NAME = "json/dndapi-skills.json"
const val DAMAGE_TYPE_JSON_FILE_NAME = "json/dndapi-damage-types.json"
const val JSON_FORMATTED_FILE_NAME = "json/monsters.json"

val contentType: MediaType = MediaType.get("application/json")
val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
}

@ExperimentalSerializationApi
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://www.dnd5eapi.co/api/")
    .addConverterFactory(json.asConverterFactory(contentType))
    .build()

suspend fun start(block: suspend CoroutineScope.() -> Unit) = coroutineScope {
    val startTime = System.currentTimeMillis()
    println("Started\n")
    block()
    println("\nFinished in ${System.currentTimeMillis() - startTime} ms")
}

inline fun <reified T> saveJsonFile(monsters: List<T>, fileName: String) {
    println("\nSaving")
    val json = json.encodeToString(monsters)
    saveJsonFile(fileName, json)
    println()
    println("Json saved")
}

fun saveJsonFile(fileName: String, json: String) {
    File(fileName).writeText(json)
    println(json)
}

fun readJsonFile(fileName: String): String = File(fileName).readText()

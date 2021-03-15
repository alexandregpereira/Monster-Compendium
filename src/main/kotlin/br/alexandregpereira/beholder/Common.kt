package br.alexandregpereira.beholder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import java.io.File

const val JSON_FILE_NAME = "json/dndapi-monsters.json"
const val JSON_FORMATTED_FILE_NAME = "json/monsters.json"

val contentType: MediaType = MediaType.get("application/json")
val json = Json { ignoreUnknownKeys = true }

suspend fun start(block: suspend CoroutineScope.() -> Unit) = coroutineScope {
    val startTime = System.currentTimeMillis()
    println("Started\n")
    block()
    println("\nFinished in ${System.currentTimeMillis() - startTime} ms")
}

inline fun <reified T> saveJsonFile(monsters: List<T>, fileName: String) {
    println("\nSaving")
    val json = Json.encodeToString(monsters)
    saveJsonFile(fileName, json)
    println()
    println("Json saved")
}

fun saveJsonFile(fileName: String, json: String) {
    File(fileName).writeText(json)
    println(json)
}

fun readJsonFile(fileName: String): String = File(fileName).readLines().first()

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
const val CONDITION_JSON_FILE_NAME = "json/dndapi-conditions.json"
const val SKILL_JSON_FILE_NAME = "json/dndapi-skills.json"
const val DAMAGE_TYPE_JSON_FILE_NAME = "json/dndapi-damage-types.json"
const val JSON_FORMATTED_FILE_NAME = "json/monsters.json"
const val JSON_IMAGES_FILE_NAME = "json/monster-images.json"

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

inline fun <reified T> saveJsonFile(values: T, fileName: String, printJson: Boolean = true) {
    println("\nSaving")
    val json = json.encodeToString(values)
    saveJsonFile(fileName, json, printJson)
    println()
    println("Json saved")
}

fun saveJsonFile(fileName: String, json: String, printJson: Boolean = true) {
    File(fileName).writeText(json)
    if (printJson) println(json)
}

fun readJsonFile(fileName: String): String = File(fileName).readText()

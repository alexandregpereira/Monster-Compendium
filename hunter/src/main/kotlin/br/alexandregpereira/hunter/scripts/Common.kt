/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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

const val MONSTER_JSON_FILE_NAME = "monsters.json"

const val JSON_FILE_NAME = "json/dndapi-monsters.json"
const val CONDITION_JSON_FILE_NAME = "json/dndapi-conditions.json"
const val SKILL_JSON_FILE_NAME = "json/dndapi-skills.json"
const val DAMAGE_TYPE_JSON_FILE_NAME = "json/dndapi-damage-types.json"
const val JSON_FORMATTED_FILE_NAME = "json/$MONSTER_JSON_FILE_NAME"
const val JSON_IMAGES_FILE_NAME = "json/monster-images.json"
const val ALTERNATIVE_SOURCES_JSON_FILE_NAME = "json/alternative-sources.json"
const val BESTIARY_PART1_JSON_FILE_NAME = "json/5e-bestiary-div1.json"
const val BESTIARY_PART2_JSON_FILE_NAME = "json/5e-bestiary-div2.json"
const val BESTIARY_PART3_JSON_FILE_NAME = "json/5e-bestiary-div3.json"
const val BESTIARY_PART4_JSON_FILE_NAME = "json/5e-bestiary-div4.json"

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

@OptIn(ExperimentalSerializationApi::class)
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

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

package br.alexandregpereira.hunter.bestiary

import br.alexandregpereira.hunter.scripts.BESTIARY_PART1_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.BESTIARY_PART2_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.BESTIARY_PART3_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.BESTIARY_PART4_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.json
import br.alexandregpereira.hunter.scripts.readJsonFile
import br.alexandregpereira.hunter.scripts.start
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.serialization.decodeFromString

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {

    getMonstersFromBestiary()
        .collect {
            println(it)
            println(it.size)
        }
}

@FlowPreview
suspend fun getMonstersFromBestiary(): Flow<List<Monster>> {
    return getSourceFromBestiary()
        .map { sources ->
            sources.map { source ->
                source.monster.map { monster ->
                    monster.copy(sourceName = source.name)
                }
            }.reduce { acc, list -> acc + list }
        }.reduce { acc, list -> acc + list }.run {
            flowOf(this)
        }
        .map { monsters ->
            monsters.filter { it.srd.not() }.map {
                it.copy(name = it.name.replace("\"", ""))
            }
        }
}

@FlowPreview
suspend fun getSourceFromBestiary(): Flow<List<SourceItem>> {
    return listOf(
        BESTIARY_PART1_JSON_FILE_NAME,
        BESTIARY_PART2_JSON_FILE_NAME,
        BESTIARY_PART3_JSON_FILE_NAME,
        BESTIARY_PART4_JSON_FILE_NAME,
    ).asFlow()
        .flatMapMerge {
            flow { emit(getBestiarySources(fileName = it)) }
        }
        .reduce { accumulator, value -> accumulator + value }
        .run {
            flowOf(this)
        }
}

private fun getBestiarySources(fileName: String): List<SourceItem> {
    return json.decodeFromString(readJsonFile(fileName))
}
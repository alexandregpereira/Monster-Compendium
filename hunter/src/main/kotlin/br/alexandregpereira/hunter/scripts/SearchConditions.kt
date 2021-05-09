package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.dndapi.data.model.Monster
import kotlinx.serialization.decodeFromString

suspend fun main() = start {
    json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asSequence()
        .map { it.conditionImmunities }
        .reduce { acc, list -> acc + list }
        .map { it.name }
        .toSet()
        .sorted()
        .forEach { conditionName ->
            println(conditionName)
        }
}
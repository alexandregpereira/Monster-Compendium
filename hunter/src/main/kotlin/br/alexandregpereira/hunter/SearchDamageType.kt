package br.alexandregpereira.hunter

import br.alexandregpereira.hunter.dndapi.data.Monster
import kotlinx.serialization.decodeFromString

suspend fun main() = start {
    json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asSequence()
        .map { it.damageResistances + it.damageImmunities + it.damageVulnerabilities }
        .reduce { acc, list -> acc + list }
        .toSet()
        .sorted()
        .toList()
        .forEach {
            println(it)
        }
}
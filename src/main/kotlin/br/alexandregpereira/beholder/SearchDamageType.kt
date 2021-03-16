package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.Monster
import kotlinx.serialization.decodeFromString

suspend fun main() = start {
    json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .asSequence()
        .filter { it.damageResistances.isNotEmpty() }
        .map { it.damageResistances + it.damageImmunities + it.damageVulnerabilities }
        .reduce { acc, list -> acc + list }
        .toSet()
        .sorted()
        .toList()
        .forEach {
            println(it)
        }
}
package br.alexandregpereira.beholder

import br.alexandregpereira.beholder.dndapi.data.Monster
import kotlinx.serialization.decodeFromString

suspend fun main() = start {
    json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME))
        .filter { it.subtype.isNullOrEmpty().not() }
        .sortedBy { it.subtype }
        .forEach {
            print(it.subtype)
            print(" --> ")
            println(it.name)
        }
}
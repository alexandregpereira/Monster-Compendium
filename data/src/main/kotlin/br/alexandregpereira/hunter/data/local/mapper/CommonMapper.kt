package br.alexandregpereira.hunter.data.local.mapper

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal inline fun <reified T> List<T>.toJsonFromList(): String {
    return Json.encodeToString(this)
}

internal inline fun <reified T> String.toListFromJson(): List<T> {
    return Json.decodeFromString(this)
}

package br.alexandregpereira.hunter.data.local.mapper

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal inline fun <reified T> T.toJsonFromObj(): String {
    return Json.encodeToString(this)
}

internal inline fun <reified T> String.toObjFromJson(): T {
    return Json.decodeFromString(this)
}

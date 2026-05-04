package br.alexandregpereira.hunter.shareContent.domain

import kotlinx.serialization.json.Json

internal val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

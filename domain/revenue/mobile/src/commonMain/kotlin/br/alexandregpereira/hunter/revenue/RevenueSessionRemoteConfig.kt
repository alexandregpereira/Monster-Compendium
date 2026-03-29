/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.revenue

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class RevenueSessionRemoteConfig(
    private val client: HttpClient,
    private val json: Json,
) {

    suspend fun getSessionTimeLimitInMillis(): Long {
        return try {
            val body: String = client.get(
                urlString = "remote-config.json"
            ).bodyAsText()
            val json = json.decodeFromString<JsonObject>(body)
            val key = "sessionTimeLimitInMillis"
            json[key]
                ?.jsonPrimitive
                ?.content
                ?.toLongOrNull() ?: throw RevenueSessionRemoteConfigException.MissingConfigKey(
                    key = key,
                )
        } catch (cause: Throwable) {
            throw RevenueSessionRemoteConfigException.FailToFetchConfig(cause)
        }
    }
}

internal sealed class RevenueSessionRemoteConfigException(
    message: String,
    cause: Throwable? = null
) : Throwable(message, cause) {

    class FailToFetchConfig(cause: Throwable) : RevenueSessionRemoteConfigException(
        message = "Failed to fetch revenue session remote config. cause: ${cause.message}",
        cause = cause,
    )

    class MissingConfigKey(key: String) : RevenueSessionRemoteConfigException(
        message = "Missing config key: $key",
    )
}

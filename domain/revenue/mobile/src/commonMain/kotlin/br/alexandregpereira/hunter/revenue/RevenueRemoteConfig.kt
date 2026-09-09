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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * Reads the remote config document that tunes the monetization behavior. The document is fetched
 * at most once per process: the http client has no cache plugin and more than one config value is
 * read per paywall check, so without this every check would download the document again.
 */
internal class RevenueRemoteConfig(
    private val client: HttpClient,
    private val json: Json,
) {

    private val mutex = Mutex()
    private var cached: JsonObject? = null

    suspend fun getSessionTimeLimitInMillis(): Long {
        val key = "sessionTimeLimitInMillis"
        val element = getConfig()[key] ?: throw RevenueRemoteConfigException.MissingConfigKey(key)
        return try {
            element.jsonPrimitive.content.toLongOrNull()
                ?: throw RevenueRemoteConfigException.MissingConfigKey(key)
        } catch (cause: RevenueRemoteConfigException) {
            throw cause
        } catch (cause: Throwable) {
            throw RevenueRemoteConfigException.FailToParseConfig(cause)
        }
    }

    suspend fun getPaywallCooldownIntervalsInMillis(): List<Long> {
        val key = "paywallCooldownIntervalsInMillis"
        val element = getConfig()[key] ?: throw RevenueRemoteConfigException.MissingConfigKey(key)
        return try {
            element.jsonArray.mapNotNull { item ->
                item.jsonPrimitive.content.toLongOrNull()?.takeIf { it > 0 }
            }
        } catch (cause: Throwable) {
            throw RevenueRemoteConfigException.FailToParseConfig(cause)
        }
    }

    private suspend fun getConfig(): JsonObject = mutex.withLock {
        cached?.let { return@withLock it }
        val body: String = try {
            client.get(urlString = "remote-config.json").bodyAsText()
        } catch (cause: Throwable) {
            throw RevenueRemoteConfigException.FailToFetchConfig(cause)
        }
        val config = try {
            json.decodeFromString<JsonObject>(body)
        } catch (cause: Throwable) {
            throw RevenueRemoteConfigException.FailToParseConfig(cause)
        }
        cached = config
        config
    }
}

internal sealed class RevenueRemoteConfigException(
    message: String,
    cause: Throwable? = null
) : Throwable(message, cause) {

    class FailToFetchConfig(cause: Throwable) : RevenueRemoteConfigException(
        message = "Failed to fetch the revenue remote config. cause: ${cause.message}",
        cause = cause,
    )

    class FailToParseConfig(cause: Throwable) : RevenueRemoteConfigException(
        message = "Failed to parse the revenue remote config json. cause: ${cause.message}",
        cause = cause,
    )

    class MissingConfigKey(key: String) : RevenueRemoteConfigException(
        message = "Missing config key: $key",
    )
}

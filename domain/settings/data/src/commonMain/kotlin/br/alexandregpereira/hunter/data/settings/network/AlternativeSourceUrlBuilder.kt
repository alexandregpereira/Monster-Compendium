/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.settings.network

import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single

class AlternativeSourceUrlBuilder(
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase
) {

    suspend fun execute(
        requestBuilder: HttpRequestBuilder
    ): HttpRequestBuilder {
        val currentUrl = requestBuilder.url.buildString()
        val newRequestBuilder = if (isToInterceptUrl(currentUrl)) {
            val newUrl = getNewUrl(currentUrl)
            requestBuilder.apply {
                url {
                    host = newUrl.split("/").firstOrNull().orEmpty()
                    path(*newUrl.split("/").drop(1).toTypedArray())
                }
            }
        } else requestBuilder

        return newRequestBuilder
    }

    private fun isToInterceptUrl(url: String): Boolean {
        return when {
            url.contains("alternative-sources.json") -> true
            url.contains("/sources/") -> true
            else -> false
        }
    }

    private suspend fun getNewUrl(currentUrl: String): String {
        return when {
            currentUrl.contains("alternative-sources.json") -> getAlternativeSourceJsonUrl()
            currentUrl.contains("/sources/") -> getAlternativeSourceJsonUrl().map { url ->
                if (url.isBlank()) return@map url

                val path = currentUrl.split("/")
                    .run { subList(size - 4, size) }
                    .joinToString("/")
                val host = url.split("/")
                    .run { subList(0, size - 1) }
                    .joinToString("/")

                "$host/$path"
            }
            else -> null
        }?.appendCurrentUrlIfEmpty(currentUrl)?.single()
            ?.replace("https://", "")
            ?.replace("http://", "") ?: currentUrl
    }

    private fun Flow<String>.appendCurrentUrlIfEmpty(currentUrl: String): Flow<String> {
        return map { url ->
            url.ifBlank { currentUrl }
        }
    }
}

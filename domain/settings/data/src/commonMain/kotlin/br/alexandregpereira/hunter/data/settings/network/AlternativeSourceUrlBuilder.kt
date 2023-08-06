/*
 * Copyright 2022 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

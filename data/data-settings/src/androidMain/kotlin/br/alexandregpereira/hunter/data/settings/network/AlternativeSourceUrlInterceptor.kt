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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AlternativeSourceUrlInterceptor(
    private val getAlternativeSourceJsonUrl: GetAlternativeSourceJsonUrlUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val currentUrl = request.url.toString()
        val newRequest = currentUrl.takeIf { url ->
            isToInterceptUrl(url)
        }?.let { url ->
            getNewUrl(url)
        }?.let { newUrl ->
            request.newBuilder().url(newUrl).build()
        }

        return chain.proceed(newRequest ?: request)
    }

    private fun isToInterceptUrl(url: String): Boolean {
        return when {
            url.contains("alternative-sources.json") -> true
            url.contains("/sources/") -> true
            else -> false
        }
    }

    private fun getNewUrl(currentUrl: String): String? = runBlocking {
        when {
            currentUrl.contains("alternative-sources.json") -> getAlternativeSourceJsonUrl()
            currentUrl.contains("/sources/") -> getAlternativeSourceJsonUrl().map { url ->
                val path = currentUrl.split("/")
                    .run { subList(size - 4, size) }
                    .joinToString("/")
                val urlFormatted = url.split("/")
                    .run { subList(0, size - 1) }
                    .joinToString("/")

                "$urlFormatted/$path"
            }
            else -> null
        }?.appendLocalHostIfEmpty()?.single()
    }

    private fun Flow<String>.appendLocalHostIfEmpty(): Flow<String> {
        return map { url ->
            url.ifBlank {
                "https://localhost/"
            }
        }
    }
}

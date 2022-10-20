/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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

import br.alexandregpereira.hunter.data.settings.DEFAULT_API_BASE_URL
import br.alexandregpereira.hunter.domain.settings.GetAlternativeSourceJsonUrlUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AlternativeSourceUrlInterceptor @Inject constructor(
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
                currentUrl.replace(
                    DEFAULT_API_BASE_URL,
                    url.replace("alternative-sources.json", "")
                )
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

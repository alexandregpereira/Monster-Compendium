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

package br.alexandregpereira.hunter.data.source.remote

import br.alexandregpereira.hunter.data.source.remote.model.AlternativeSourceDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal class DefaultAlternativeSourceRemoteDataSource(
    private val client: HttpClient,
    private val json: Json
) : AlternativeSourceRemoteDataSource {

    override suspend fun getAlternativeSources(lang: String): List<AlternativeSourceDto> {
        return json.decodeFromString(
            client.get(urlString = "$lang/content-sources.json").bodyAsText()
        )
    }

    override suspend fun getBasicAlternativeSources(lang: String): List<AlternativeSourceDto> {
        return json.decodeFromString(
            client.get(urlString = "$lang/content-sources-basic.json").bodyAsText()
        )
    }
}

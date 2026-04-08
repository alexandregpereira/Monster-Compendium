package br.alexandregpereira.hunter.data.strings

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal class StringsRemoteDataSource(
    private val client: HttpClient,
    private val json: Json,
) {
    fun getStrings(lang: String): Flow<Map<String, String>> = flow {
        emit(json.decodeFromString(client.get("strings/$lang.json").bodyAsText()))
    }
}

package br.alexandregpereira.hunter.condition.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

internal interface ConditionRemoteDataSource {
    suspend fun getConditions(lang: String): List<ConditionDto>
}

internal class ConditionRemoteDataSourceImpl(
    private val client: HttpClient,
    private val json: Json,
) : ConditionRemoteDataSource {

    override suspend fun getConditions(lang: String): List<ConditionDto> {
        return json.decodeFromString(client.get("$lang/conditions.json").bodyAsText())
    }
}

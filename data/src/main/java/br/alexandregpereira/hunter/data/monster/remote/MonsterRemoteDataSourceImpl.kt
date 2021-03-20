package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class MonsterRemoteDataSourceImpl(
    private val fileManager: FileManager
) : MonsterRemoteDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun getMonsters(): Flow<List<MonsterDto>> = fileManager.readText().map { jsonValue ->
        json.decodeFromString(jsonValue)
    }

    @ExperimentalCoroutinesApi
    override fun insertMonsters(monsters: List<MonsterDto>): Flow<Unit> = flow {
        emit(json.encodeToString(monsters))
    }.flatMapLatest { json ->
        fileManager.writeText(json)
    }
}

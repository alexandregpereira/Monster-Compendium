package br.alexandregpereira.hunter.data.monster.remote

import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

internal class MonsterRemoteDataSourceImpl(
    private val file: File = File(JSON_FORMATTED_FILE_NAME)
) : MonsterRemoteDataSource {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun getMonsters(): Flow<List<MonsterDto>> = flow {
        val jsonValue = file.readText()
        emit(json.decodeFromString<List<MonsterDto>>(jsonValue))
    }

    override fun insertMonsters(monsters: List<MonsterDto>): Flow<Unit> = flow {
        val json = json.encodeToString(monsters)
        file.writeText(json)
        emit(Unit)
    }
}

private const val JSON_FORMATTED_FILE_NAME = "json/monsters.json"

package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.mapper.toDtos
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterDto
import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.serialization.json.Json

internal class MonsterContentJsonMapperImpl(
    private val json: Json,
) : MonsterContentJsonMapper {

    override suspend fun decodeFromJson(contentJson: String): List<Monster> {
        return json.decodeFromString<List<MonsterDto>>(contentJson).toDomain()
    }

    override suspend fun encodeToJson(value: List<Monster>): String {
        return json.encodeToString<List<MonsterDto>>(value.toDtos())
    }
}

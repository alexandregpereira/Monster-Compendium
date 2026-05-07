package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.data.monster.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.remote.mapper.toMonsterImageDtos
import br.alexandregpereira.hunter.data.monster.remote.model.MonsterImageDto
import br.alexandregpereira.hunter.domain.model.MonsterImage
import kotlinx.serialization.json.Json

internal class MonsterImageContentJsonMapperImpl(
    private val json: Json,
) : MonsterImageContentJsonMapper {

    override suspend fun decodeFromJson(contentJson: String): List<MonsterImage> {
        return json.decodeFromString<List<MonsterImageDto>>(contentJson).toDomain()
    }

    override suspend fun encodeToJson(value: List<MonsterImage>): String {
        return json.encodeToString<List<MonsterImageDto>>(value.toMonsterImageDtos())
    }
}

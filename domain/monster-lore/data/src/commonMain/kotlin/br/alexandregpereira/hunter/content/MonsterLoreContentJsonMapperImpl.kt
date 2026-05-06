package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.data.monster.lore.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.monster.lore.remote.mapper.toDtos
import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreDto
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore
import kotlinx.serialization.json.Json

internal class MonsterLoreContentJsonMapperImpl(
    private val json: Json,
) : MonsterLoreContentJsonMapper {

    override suspend fun decodeFromJson(contentJson: String): List<MonsterLore> {
        return json.decodeFromString<List<MonsterLoreDto>>(contentJson).map {
            it.toDomain()
        }
    }

    override suspend fun encodeToJson(value: List<MonsterLore>): String {
        return json.encodeToString<List<MonsterLoreDto>>(value.toDtos())
    }
}

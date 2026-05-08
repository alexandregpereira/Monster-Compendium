package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.data.spell.remote.mapper.toDomain
import br.alexandregpereira.hunter.data.spell.remote.mapper.toDtos
import br.alexandregpereira.hunter.data.spell.remote.model.SpellDto
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.serialization.json.Json

internal class SpellContentJsonMapperImpl(
    private val json: Json,
) : SpellContentJsonMapper {

    override suspend fun decodeFromJson(contentJson: String): List<Spell> {
        return json.decodeFromString<List<SpellDto>>(contentJson).toDomain()
    }

    override suspend fun encodeToJson(value: List<Spell>): String {
        return json.encodeToString<List<SpellDto>>(value.toDtos())
    }
}

package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.domain.spell.model.Spell

interface SpellContentJsonMapper {

    suspend fun decodeFromJson(contentJson: String): List<Spell>

    suspend fun encodeToJson(value: List<Spell>): String
}

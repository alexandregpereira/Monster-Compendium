package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.domain.model.Monster

interface MonsterContentJsonMapper {

    suspend fun decodeFromJson(contentJson: String): List<Monster>

    suspend fun encodeToJson(value: List<Monster>): String
}

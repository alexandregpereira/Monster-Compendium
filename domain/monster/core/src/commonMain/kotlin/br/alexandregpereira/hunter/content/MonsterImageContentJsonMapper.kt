package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.domain.model.MonsterImage

interface MonsterImageContentJsonMapper {

    suspend fun decodeFromJson(contentJson: String): List<MonsterImage>

    suspend fun encodeToJson(value: List<MonsterImage>): String
}

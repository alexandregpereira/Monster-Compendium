package br.alexandregpereira.hunter.content

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore

interface MonsterLoreContentJsonMapper {

    suspend fun decodeFromJson(contentJson: String): List<MonsterLore>

    suspend fun encodeToJson(value: List<MonsterLore>): String
}

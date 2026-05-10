package br.alexandregpereira.hunter.data.monster.lore.remote.mapper

import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreDto
import br.alexandregpereira.hunter.data.monster.lore.remote.model.MonsterLoreEntryDto
import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLore

internal fun List<MonsterLore>.toDtos(): List<MonsterLoreDto> {
    return map { it.toDto() }
}

internal fun MonsterLore.toDto(): MonsterLoreDto {
    return MonsterLoreDto(
        index = index,
        entries = entries.map { MonsterLoreEntryDto(title = it.title, description = it.description) },
        status = status.name,
    )
}

package br.alexandregpereira.hunter.monster.lore.registration.mapper

import br.alexandregpereira.hunter.domain.monster.lore.model.MonsterLoreEntry
import br.alexandregpereira.hunter.monster.lore.registration.MonsterLoreEntryState

internal fun List<MonsterLoreEntry>.asState(): List<MonsterLoreEntryState> = map { it.asState() }

internal fun MonsterLoreEntry.asState(): MonsterLoreEntryState = MonsterLoreEntryState(
    title = title,
    description = description,
)

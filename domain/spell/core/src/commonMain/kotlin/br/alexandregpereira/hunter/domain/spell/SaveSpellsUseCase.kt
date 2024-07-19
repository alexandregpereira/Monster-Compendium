package br.alexandregpereira.hunter.domain.spell

import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow

fun interface SaveSpells {
    operator fun invoke(spells: List<Spell>): Flow<Unit>
}

internal fun SaveSpells(
    spellRepository: SpellLocalRepository,
): SaveSpells = SaveSpells { spells ->
    spellRepository.saveSpells(spells)
}

package br.alexandregpereira.hunter.spell.compendium.domain

import br.alexandregpereira.hunter.domain.spell.SpellLocalRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow

internal fun interface GetSpellsUseCase {

    operator fun invoke(): Flow<List<Spell>>
}

internal fun GetSpellsUseCase(repository: SpellLocalRepository): GetSpellsUseCase {
    return GetSpellsUseCase { repository.getLocalSpells() }
}

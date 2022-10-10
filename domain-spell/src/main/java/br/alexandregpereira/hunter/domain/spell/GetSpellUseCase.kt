package br.alexandregpereira.hunter.domain.spell

import br.alexandregpereira.hunter.domain.spell.model.Spell
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSpellUseCase @Inject constructor(
    private val spellRepository: SpellRepository
) {

    operator fun invoke(spellIndex: String): Flow<Spell> {
        return spellRepository.getLocalSpell(spellIndex)
    }
}

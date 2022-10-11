package br.alexandregpereira.hunter.domain.spell

import br.alexandregpereira.hunter.domain.spell.model.Spell
import kotlinx.coroutines.flow.Flow

interface SpellRepository {

    fun saveSpells(spells: List<Spell>): Flow<Unit>
    fun getRemoteSpells(): Flow<List<Spell>>
    fun getLocalSpell(index: String): Flow<Spell>
    fun deleteLocalSpells(): Flow<Unit>
}

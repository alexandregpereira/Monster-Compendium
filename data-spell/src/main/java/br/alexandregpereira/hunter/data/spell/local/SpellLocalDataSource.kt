package br.alexandregpereira.hunter.data.spell.local

import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import kotlinx.coroutines.flow.Flow

internal interface SpellLocalDataSource {

    fun saveSpells(spells: List<SpellEntity>): Flow<Unit>
    fun getSpell(index: String): Flow<SpellEntity>
    fun deleteSpells(): Flow<Unit>
}

package br.alexandregpereira.hunter.data.spell.local

import br.alexandregpereira.hunter.data.spell.local.dao.SpellDao
import br.alexandregpereira.hunter.data.spell.local.model.SpellEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class SpellLocalDataSourceImpl @Inject constructor(
    private val spellDao: SpellDao
) : SpellLocalDataSource {

    override fun saveSpells(spells: List<SpellEntity>): Flow<Unit> = flow {
        emit(spellDao.insert(spells))
    }

    override fun getSpell(index: String): Flow<SpellEntity> = flow {
        emit(spellDao.getSpell(index))
    }

    override fun deleteSpells(): Flow<Unit> = flow {
        emit(spellDao.deleteAll())
    }
}

package br.alexandregpereira.hunter.data.spell

import br.alexandregpereira.hunter.data.spell.local.SpellLocalDataSource
import br.alexandregpereira.hunter.data.spell.local.mapper.toDomain
import br.alexandregpereira.hunter.data.spell.local.mapper.toEntity
import br.alexandregpereira.hunter.data.spell.remote.SpellRemoteDataSource
import br.alexandregpereira.hunter.data.spell.remote.mapper.toDomain
import br.alexandregpereira.hunter.domain.spell.SpellRepository
import br.alexandregpereira.hunter.domain.spell.model.Spell
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SpellRepositoryImpl @Inject constructor(
    private val remoteDataSource: SpellRemoteDataSource,
    private val localDataSource: SpellLocalDataSource
) : SpellRepository {

    override fun saveSpells(spells: List<Spell>): Flow<Unit> {
        return localDataSource.saveSpells(spells.toEntity())
    }

    override fun getRemoteSpells(): Flow<List<Spell>> {
        return remoteDataSource.getSpells().map { it.toDomain() }
    }

    override fun getLocalSpell(index: String): Flow<Spell> {
        return localDataSource.getSpell(index).map { it.toDomain() }
    }

    override fun deleteLocalSpells(): Flow<Unit> {
        return localDataSource.deleteSpells()
    }
}

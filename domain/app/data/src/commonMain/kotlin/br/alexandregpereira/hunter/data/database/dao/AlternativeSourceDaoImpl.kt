package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.source.local.dao.AlternativeSourceDao
import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity
import br.alexandregpereira.hunter.database.AlternativeSourceQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class AlternativeSourceDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    private val queries: AlternativeSourceQueries,
) : AlternativeSourceDao {

    override suspend fun getAlternativeSources(): List<AlternativeSourceEntity> = withContext(dispatcher) {
        queries.getAll().executeAsList().map { it.toLocalEntity() }
    }

    override suspend fun addAlternativeSource(alternativeSource: AlternativeSourceEntity) = withContext(dispatcher) {
        queries.insert(alternativeSource.toDatabaseEntity())
    }

    override suspend fun removeAlternativeSource(acronym: String) = withContext(dispatcher) {
        queries.deleteByAcronym(acronym)
    }
}

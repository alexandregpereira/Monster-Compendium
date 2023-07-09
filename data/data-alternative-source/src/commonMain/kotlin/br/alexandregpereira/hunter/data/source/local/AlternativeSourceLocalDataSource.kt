package br.alexandregpereira.hunter.data.source.local

import br.alexandregpereira.hunter.data.source.local.dao.AlternativeSourceDao
import br.alexandregpereira.hunter.data.source.local.entity.AlternativeSourceEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class AlternativeSourceLocalDataSource(
    private val dao: AlternativeSourceDao
) {

    fun getAlternativeSources(): Flow<List<AlternativeSourceEntity>> = flow {
        emit(dao.getAlternativeSources())
    }

    fun addAlternativeSource(alternativeSource: AlternativeSourceEntity): Flow<Unit> = flow {
        emit(dao.addAlternativeSource(alternativeSource))
    }

    fun removeAlternativeSource(acronym: String): Flow<Unit> = flow {
        emit(dao.removeAlternativeSource(acronym))
    }
}

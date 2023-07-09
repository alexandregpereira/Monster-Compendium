package br.alexandregpereira.hunter.data.source.local

import br.alexandregpereira.hunter.data.source.local.mapper.toDomain
import br.alexandregpereira.hunter.data.source.local.mapper.toEntity
import br.alexandregpereira.hunter.domain.source.AlternativeSourceLocalRepository
import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
internal class AlternativeSourceLocalRepositoryImpl(
    private val localDataSource: AlternativeSourceLocalDataSource
) : AlternativeSourceLocalRepository {

    override fun getAlternativeSources(): Flow<List<AlternativeSource>> {
        return localDataSource.getAlternativeSources().map { it.toDomain() }
    }

    override fun addAlternativeSource(acronym: String): Flow<Unit> {
        return flowOf(acronym).map { it.toEntity() }.flatMapLatest { entity ->
            localDataSource.addAlternativeSource(entity)
        }
    }

    override fun removeAlternativeSource(acronym: String): Flow<Unit> {
        return localDataSource.removeAlternativeSource(acronym)
    }
}

package br.alexandregpereira.hunter.domain.source

import kotlinx.coroutines.flow.Flow

class RemoveAlternativeSourceUseCase(
    private val repository: AlternativeSourceLocalRepository
) {

    operator fun invoke(acronym: String): Flow<Unit> {
        return repository.removeAlternativeSource(acronym)
    }
}

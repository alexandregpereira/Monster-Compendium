package br.alexandregpereira.hunter.domain.source

import br.alexandregpereira.hunter.domain.source.model.AlternativeSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAlternativeSourcesUseCase @Inject constructor(
    private val repository: AlternativeSourceRepository
) {

    operator fun invoke(): Flow<List<AlternativeSource>> {
        return repository.getAlternativeSources()
    }
}

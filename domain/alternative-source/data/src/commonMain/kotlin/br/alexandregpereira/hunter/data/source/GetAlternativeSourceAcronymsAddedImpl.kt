package br.alexandregpereira.hunter.data.source

import br.alexandregpereira.hunter.domain.source.GetAlternativeSourceAcronymsAdded
import br.alexandregpereira.hunter.domain.source.GetAlternativeSourcesUseCase
import kotlinx.coroutines.flow.firstOrNull

internal class GetAlternativeSourceAcronymsAddedImpl(
    private val getAlternativeSourcesUseCase: GetAlternativeSourcesUseCase,
) : GetAlternativeSourceAcronymsAdded {

    override suspend fun invoke(): List<String> {
        return getAlternativeSourcesUseCase()
            .firstOrNull()
            ?.filter{ it.isAdded }
            ?.map {
                it.acronym
            }.orEmpty()
    }
}

package br.alexandregpereira.hunter.condition

import br.alexandregpereira.hunter.condition.data.remote.ConditionRemoteDataSource
import br.alexandregpereira.hunter.condition.data.remote.toDomain
import br.alexandregpereira.hunter.domain.settings.GetLanguageUseCase
import kotlinx.coroutines.flow.single

internal class GetConditionsImpl(
    private val remoteDataSource: ConditionRemoteDataSource,
    private val getLanguage: GetLanguageUseCase,
) : GetConditions {

    override suspend fun invoke(): List<Condition> {
        val lang = getLanguage().single()
        return remoteDataSource.getConditions(lang).toDomain()
    }
}

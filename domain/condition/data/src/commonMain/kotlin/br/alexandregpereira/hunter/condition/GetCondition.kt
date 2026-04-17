package br.alexandregpereira.hunter.condition

import br.alexandregpereira.hunter.condition.data.local.ConditionLocalDataSource
import br.alexandregpereira.hunter.condition.data.local.toDomain
import br.alexandregpereira.ktx.runCatching

internal class GetConditionImpl(
    private val localDataSource: ConditionLocalDataSource,
    private val syncConditions: SyncConditions,
) : GetCondition {

    override suspend fun invoke(index: String): Condition {
        val condition = getLocalCondition(index)
        if (condition != null) {
            return condition
        }
        return runCatching {
            syncConditions()
            getLocalCondition(index) ?: throw IllegalStateException("Condition not found")
        }.getOrElse { cause ->
            throw cause
        }
    }

    private suspend fun getLocalCondition(index: String): Condition? {
        return localDataSource.getCondition(index)?.toDomain()
    }
}

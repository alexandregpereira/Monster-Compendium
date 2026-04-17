package br.alexandregpereira.hunter.condition

import br.alexandregpereira.hunter.condition.data.local.ConditionLocalDataSource
import br.alexandregpereira.hunter.condition.data.local.toEntity

internal class SyncConditionsImpl(
    private val getConditions: GetConditions,
    private val localDataSource: ConditionLocalDataSource,
) : SyncConditions {

    override suspend fun invoke() {
        val conditions = getConditions()
        localDataSource.deleteAll()
        localDataSource.save(conditions.toEntity())
    }
}

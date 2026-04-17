package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.condition.data.local.ConditionEntity
import br.alexandregpereira.hunter.condition.data.local.ConditionLocalDataSource
import br.alexandregpereira.hunter.database.ConditionDataQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.ConditionDataEntity as ConditionDatabaseEntity

internal class ConditionLocalDataSourceImpl(
    private val queries: ConditionDataQueries,
    private val dispatcher: CoroutineDispatcher,
) : ConditionLocalDataSource {

    override suspend fun getCondition(index: String): ConditionEntity? = withContext(dispatcher) {
        queries.getCondition(index).executeAsOneOrNull()?.asConditionEntity()
    }

    override suspend fun save(conditions: List<ConditionEntity>): Unit = withContext(dispatcher) {
        queries.transaction {
            conditions.forEach {
                queries.insert(
                    ConditionDatabaseEntity(
                        index = it.index,
                        name = it.name,
                        type = it.type,
                        description = it.description,
                    )
                )
            }
        }
    }

    override suspend fun deleteAll() {
        withContext(dispatcher) {
            queries.deleteAll()
        }
    }
}

private fun ConditionDatabaseEntity.asConditionEntity() = ConditionEntity(
    index = index,
    name = name,
    type = type,
    description = description,
)

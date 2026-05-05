package br.alexandregpereira.hunter.condition.data.local

interface ConditionLocalDataSource {
    suspend fun getCondition(index: String): ConditionEntity?
    suspend fun save(conditions: List<ConditionEntity>)
    suspend fun deleteAll()
}

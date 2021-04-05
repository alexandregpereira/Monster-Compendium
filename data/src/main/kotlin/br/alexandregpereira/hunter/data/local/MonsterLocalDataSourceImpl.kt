package br.alexandregpereira.hunter.data.local

import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MonsterLocalDataSourceImpl(
    private val monsterDao: MonsterDao
) : MonsterLocalDataSource {

    override fun getMonsters(): Flow<List<MonsterEntity>> {
        return monsterDao.getMonsters()
    }

    override fun saveMonsters(monsters: List<MonsterEntity>): Flow<Unit> {
        return flow {
            monsterDao.insert(monsters)
            emit(Unit)
        }
    }
}
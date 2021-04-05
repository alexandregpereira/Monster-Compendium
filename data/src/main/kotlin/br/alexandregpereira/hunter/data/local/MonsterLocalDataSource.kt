package br.alexandregpereira.hunter.data.local

import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import kotlinx.coroutines.flow.Flow

internal interface MonsterLocalDataSource {

    fun getMonsters(): Flow<List<MonsterEntity>>
    fun saveMonsters(monsters: List<MonsterEntity>): Flow<Unit>
}
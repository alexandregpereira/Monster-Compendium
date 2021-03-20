package br.alexandregpereira.hunter.data.remote

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.flow.Flow

interface MonsterRemoteDataSource {

    fun getMonsters(): Flow<List<MonsterDto>>

    fun insertMonsters(monsters: List<MonsterDto>): Flow<Unit>
}
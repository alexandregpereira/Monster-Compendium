package br.alexandregpereira.hunter.data.monster.local.dao

import br.alexandregpereira.hunter.data.monster.local.entity.MonsterImageEntity

interface MonsterImageDao {
    suspend fun insert(monsterImages: List<MonsterImageEntity>)
    suspend fun deleteMonsterImage(monsterIndex: String)
    suspend fun getMonsterImage(monsterIndex: String): MonsterImageEntity?
}

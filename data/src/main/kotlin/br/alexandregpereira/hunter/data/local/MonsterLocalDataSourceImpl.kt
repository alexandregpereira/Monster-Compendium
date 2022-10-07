/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.local

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import br.alexandregpereira.hunter.data.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.local.dao.ActionDao
import br.alexandregpereira.hunter.data.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.local.dao.DamageDao
import br.alexandregpereira.hunter.data.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.local.dao.SkillDao
import br.alexandregpereira.hunter.data.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.local.entity.MonsterCompleteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

internal class MonsterLocalDataSourceImpl @Inject constructor(
    private val abilityScoreDao: AbilityScoreDao,
    private val actionDao: ActionDao,
    private val conditionDao: ConditionDao,
    private val damageDao: DamageDao,
    private val damageDiceDao: DamageDiceDao,
    private val monsterDao: MonsterDao,
    private val savingThrowDao: SavingThrowDao,
    private val skillDao: SkillDao,
    private val specialAbilityDao: SpecialAbilityDao,
    private val speedDao: SpeedDao,
    private val speedValueDao: SpeedValueDao,
    private val reactionDao: ReactionDao,
) : MonsterLocalDataSource {

    private val mutex = Mutex()

    override fun getMonsters(): Flow<List<MonsterCompleteEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonsters()
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun getMonster(index: String): Flow<MonsterCompleteEntity> = flow {
        emit(monsterDao.getMonster(index))
    }

    override fun getMonstersByQuery(query: String): Flow<List<MonsterCompleteEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonstersByQuery(
                SimpleSQLiteQuery(
                    "SELECT * FROM MonsterEntity WHERE $query"
                )
            )
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun saveMonsters(
        monsters: List<MonsterCompleteEntity>,
        isSync: Boolean
    ): Flow<Unit> = flow {
        mutex.withLock {
            if (isSync) {
                val startTime = System.currentTimeMillis()
                abilityScoreDao.deleteAll()
                actionDao.deleteAll()
                conditionDao.deleteAll()
                damageDao.deleteAllResistances()
                damageDao.deleteAllImmunities()
                damageDao.deleteAllVulnerabilities()
                damageDiceDao.deleteAll()
                savingThrowDao.deleteAll()
                skillDao.deleteAll()
                specialAbilityDao.deleteAll()
                speedDao.deleteAll()
                speedValueDao.deleteAll()
                monsterDao.deleteAll()
                reactionDao.deleteAll()
                Log.d("saveMonsters", "deleteAll in ${System.currentTimeMillis() - startTime} ms")
            }

            val startTime = System.currentTimeMillis()
            monsterDao.insert(monsters.map { it.monster })
            abilityScoreDao.insert(monsters.map { it.abilityScores }.reduceList())
            actionDao.insert(monsters.map { it.actions }.reduceList().map { it.action })
            damageDiceDao.insert(monsters.map { it.actions }.reduceList().map { it.damageDices }
                .reduceList())
            savingThrowDao.insert(monsters.map { it.savingThrows }.reduceList())
            skillDao.insert(monsters.map { it.skills }.reduceList())
            specialAbilityDao.insert(monsters.map { it.specialAbilities }.reduceList())
            speedDao.insert(monsters.mapNotNull { it.speed?.speed })
            speedValueDao.insert(monsters.mapNotNull { it.speed?.values }.reduceList())
            damageDao.insertImmunity(monsters.map { it.damageImmunities }.reduceList())
            damageDao.insertResistance(monsters.map { it.damageResistances }.reduceList())
            damageDao.insertVulnerability(monsters.map { it.damageVulnerabilities }.reduceList())
            conditionDao.insert(monsters.map { it.conditionImmunities }.reduceList())
            reactionDao.insert(monsters.map { it.reactions }.reduceList())

            Log.d("saveMonsters", "insert in ${System.currentTimeMillis() - startTime} ms")
        }
        emit(Unit)
    }

    private fun <T> List<List<T>>.reduceList(): List<T> {
        return this.reduce { acc, list -> acc + list }
    }
}

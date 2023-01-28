/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.data.monster.local

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import br.alexandregpereira.hunter.data.monster.local.dao.AbilityScoreDao
import br.alexandregpereira.hunter.data.monster.local.dao.ActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.ConditionDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDao
import br.alexandregpereira.hunter.data.monster.local.dao.DamageDiceDao
import br.alexandregpereira.hunter.data.monster.local.dao.LegendaryActionDao
import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.dao.ReactionDao
import br.alexandregpereira.hunter.data.monster.local.dao.SavingThrowDao
import br.alexandregpereira.hunter.data.monster.local.dao.SkillDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpecialAbilityDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedDao
import br.alexandregpereira.hunter.data.monster.local.dao.SpeedValueDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellUsageDao
import br.alexandregpereira.hunter.data.monster.spell.local.dao.SpellcastingDao
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellPreviewEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MonsterLocalDataSourceImpl(
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
    private val spellcastingDao: SpellcastingDao,
    private val spellUsageDao: SpellUsageDao,
    private val legendaryActionDao: LegendaryActionDao,
) : MonsterLocalDataSource {

    private val mutex = Mutex()

    override fun getMonsterPreviews(): Flow<List<MonsterEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonsterPreviews()
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun getMonsters(): Flow<List<MonsterCompleteEntity>> = flow {
        mutex.withLock {
            monsterDao.getMonsters()
        }.let { monsters ->
            emit(monsters)
        }
    }

    override fun getMonsters(indexes: List<String>): Flow<List<MonsterCompleteEntity>> = flow {
        emit(monsterDao.getMonsters(indexes))
    }

    override fun getMonster(index: String): Flow<MonsterCompleteEntity> = flow {
        emit(monsterDao.getMonster(index))
    }

    override fun getMonstersByQuery(query: String): Flow<List<MonsterEntity>> = flow {
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
                deleteAllData()
            }

            insertAll(monsters)
        }
        emit(Unit)
    }

    override fun deleteAll(): Flow<Unit> = flow {
        emit(deleteAllData())
    }

    private suspend fun deleteAllData() {
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
        spellcastingDao.deleteAll()
        spellcastingDao.deleteAllSpellUsageCrossReferences()
        spellUsageDao.deleteAll()
        spellUsageDao.deleteAllSpellCrossReferences()
        legendaryActionDao.deleteAll()
        Log.d("saveMonsters", "deleteAll in ${System.currentTimeMillis() - startTime} ms")
    }

    private suspend fun insertAll(monsters: List<MonsterCompleteEntity>) {
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
        legendaryActionDao.insert(
            monsters.map { it.legendaryActions }.reduceList().map { it.action }
        )

        spellcastingDao.insert(
            monsters.map { entity ->
                entity.spellcastings.map { it.spellcasting }
            }.reduceList()
        )
        spellUsageDao.insert(
            monsters.map { entity ->
                entity.spellcastings.map { it.usages }.map { spellUsageEntities ->
                    spellUsageEntities.map { it.spellUsage }
                }.reduceList()
            }.reduceList()
        )

        monsters.map { entity -> entity.spellcastings }.reduceList().map {
            it.usages.map { usage ->
                SpellcastingSpellUsageCrossRefEntity(
                    spellcastingId = usage.spellUsage.spellcastingId,
                    spellUsageId = usage.spellUsage.spellUsageId
                )
            }
        }.reduceList().let {
            spellcastingDao.insertSpellUsageCrossReferences(it)
        }

        val spellPreviews = mutableListOf<SpellPreviewEntity>()
        monsters.map { entity -> entity.spellcastings.map { it.usages }.reduceList() }.reduceList()
            .map {
                it.spells.map { spell ->
                    spellPreviews.add(spell)
                    SpellUsageSpellCrossRefEntity(
                        spellUsageId = it.spellUsage.spellUsageId,
                        spellIndex = spell.spellIndex
                    )
                }
            }.reduceList().let {
                spellUsageDao.insertSpellCrossReferences(it)
            }

        spellUsageDao.insertSpellPreviews(spellPreviews)

        Log.d("saveMonsters", "insert in ${System.currentTimeMillis() - startTime} ms")
    }

    private fun <T> List<List<T>>.reduceList(): List<T> {
        return this.reduceOrNull { acc, list -> acc + list } ?: emptyList()
    }
}

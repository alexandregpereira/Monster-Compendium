/*
 * Copyright 2022 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.data.database.dao

import br.alexandregpereira.hunter.data.monster.local.dao.MonsterDao
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterCompleteEntity
import br.alexandregpereira.hunter.data.monster.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellUsageSpellCrossRefEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingSpellUsageCrossRefEntity
import br.alexandregpereira.hunter.database.AbilityScoreQueries
import br.alexandregpereira.hunter.database.ActionQueries
import br.alexandregpereira.hunter.database.ConditionQueries
import br.alexandregpereira.hunter.database.DamageDiceQueries
import br.alexandregpereira.hunter.database.DamageImmunityQueries
import br.alexandregpereira.hunter.database.DamageResistanceQueries
import br.alexandregpereira.hunter.database.DamageVulnerabilityQueries
import br.alexandregpereira.hunter.database.LegendaryActionQueries
import br.alexandregpereira.hunter.database.MonsterQueries
import br.alexandregpereira.hunter.database.ReactionQueries
import br.alexandregpereira.hunter.database.SavingThrowQueries
import br.alexandregpereira.hunter.database.SkillQueries
import br.alexandregpereira.hunter.database.SpecialAbilityQueries
import br.alexandregpereira.hunter.database.SpeedQueries
import br.alexandregpereira.hunter.database.SpeedValueQueries
import br.alexandregpereira.hunter.database.SpellPreviewQueries
import br.alexandregpereira.hunter.database.SpellUsageQueries
import br.alexandregpereira.hunter.database.SpellUsageSpellCrossRefQueries
import br.alexandregpereira.hunter.database.SpellcastingQueries
import br.alexandregpereira.hunter.database.SpellcastingSpellUsageCrossRefQueries
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import br.alexandregpereira.hunter.database.MonsterEntity as MonsterDatabaseEntity

internal class MonsterDaoImpl(
    private val monsterQueries: MonsterQueries,
    private val abilityScoreQueries: AbilityScoreQueries,
    private val actionQueries: ActionQueries,
    private val conditionQueries: ConditionQueries,
    private val damageImmunityQueries: DamageImmunityQueries,
    private val damageResistanceQueries: DamageResistanceQueries,
    private val damageVulnerabilityQueries: DamageVulnerabilityQueries,
    private val damageDiceQueries: DamageDiceQueries,
    private val savingThrowQueries: SavingThrowQueries,
    private val skillQueries: SkillQueries,
    private val specialAbilityQueries: SpecialAbilityQueries,
    private val speedQueries: SpeedQueries,
    private val speedValueQueries: SpeedValueQueries,
    private val reactionQueries: ReactionQueries,
    private val spellcastingQueries: SpellcastingQueries,
    private val spellUsageQueries: SpellUsageQueries,
    private val spellcastingSpellUsageCrossRefQueries: SpellcastingSpellUsageCrossRefQueries,
    private val spellUsageCrossRefQueries: SpellUsageSpellCrossRefQueries,
    private val spellPreviewQueries: SpellPreviewQueries,
    private val legendaryActionQueries: LegendaryActionQueries,
    private val dispatcher: CoroutineDispatcher
) : MonsterDao {

    override suspend fun getMonsterPreviews(): List<MonsterEntity> = withContext(dispatcher) {
        monsterQueries.getMonsterPreviews().executeAsList().map { it.toLocalEntity() }
    }

    override suspend fun getMonsterPreviews(indexes: List<String>): List<MonsterEntity> =
        withContext(dispatcher) {
            monsterQueries.getMonsterPreviewsByIndexes(indexes).executeAsList()
                .map { it.toLocalEntity() }
        }

    override suspend fun getMonsters(): List<MonsterCompleteEntity> = withContext(dispatcher) {
        monsterQueries.getMonsters().executeAsList().queryMonsterCompleteEntities()
    }

    override suspend fun getMonsters(indexes: List<String>): List<MonsterCompleteEntity> =
        withContext(dispatcher) {
            monsterQueries.transactionWithResult {
                monsterQueries.getMonsterPreviewsByIndexes(indexes).executeAsList().run {
                    queryMonsterCompleteEntities()
                }
            }
        }

    override suspend fun getMonster(index: String): MonsterCompleteEntity =
        withContext(dispatcher) {
            monsterQueries.getMonster(index).executeAsList().queryMonsterCompleteEntities().first()
        }

    override suspend fun getMonstersByQuery(query: String): List<MonsterEntity> =
        withContext(dispatcher) {
            emptyList()
        }

    override suspend fun insert(monsters: List<MonsterCompleteEntity>) = withContext(dispatcher) {
        monsterQueries.transaction {
            monsterQueries.insert(monsters.map { it.monster })
            abilityScoreQueries.insert(monsters.mapAndReduce { abilityScores })
            actionQueries.run {
                val actions = monsters.mapAndReduce { actions }
                insert(actions.map { it.action })
                damageDiceQueries.insert(actions.mapAndReduce { damageDices })
            }
            savingThrowQueries.insert(monsters.mapAndReduce { savingThrows })
            skillQueries.insert(monsters.mapAndReduce { skills })
            specialAbilityQueries.insert(monsters.mapAndReduce { specialAbilities })
            speedQueries.insert(monsters.mapNotNull { it.speed?.speed })
            speedValueQueries.insert(monsters.mapNotNull { it.speed?.values }.reduceList())
            damageImmunityQueries.insert(monsters.mapAndReduce { damageImmunities })
            damageResistanceQueries.insert(monsters.mapAndReduce { damageResistances })
            damageVulnerabilityQueries.insert(monsters.mapAndReduce { damageVulnerabilities })
            conditionQueries.insert(monsters.mapAndReduce { conditionImmunities })
            reactionQueries.insert(monsters.mapAndReduce { reactions })

            val legendaryActions = monsters.mapAndReduce { legendaryActions }
            legendaryActionQueries.insert(legendaryActions.map { it.action })
            damageDiceQueries.insert(legendaryActions.mapAndReduce { damageDices })

            val spellcastings = monsters.mapAndReduce { spellcastings }
            spellcastingQueries.insert(spellcastings.map { it.spellcasting })

            val usages = spellcastings.mapAndReduce { usages }
            spellUsageQueries.insert(usages.map { it.spellUsage })

            val spellUsageCrossRefs = usages.map { usage ->
                SpellcastingSpellUsageCrossRefEntity(
                    spellcastingId = usage.spellUsage.spellcastingId,
                    spellUsageId = usage.spellUsage.spellUsageId
                )
            }
            spellcastingSpellUsageCrossRefQueries.insert(spellUsageCrossRefs)

            val spellsAndCrossRefs = usages.mapAndReduce {
                spells.map { spell ->
                    spell to SpellUsageSpellCrossRefEntity(
                        spellUsageId = spellUsage.spellUsageId, spellIndex = spell.spellIndex
                    )
                }
            }
            spellPreviewQueries.insert(spellsAndCrossRefs.map { it.first })
            spellUsageCrossRefQueries.insert(spellsAndCrossRefs.map { it.second })
        }
    }

    override suspend fun deleteAll() = withContext(dispatcher) {
        monsterQueries.transaction {
            abilityScoreQueries.deleteAll()
            actionQueries.deleteAll()
            conditionQueries.deleteAll()
            damageResistanceQueries.deleteAll()
            damageImmunityQueries.deleteAll()
            damageVulnerabilityQueries.deleteAll()
            damageDiceQueries.deleteAll()
            savingThrowQueries.deleteAll()
            skillQueries.deleteAll()
            specialAbilityQueries.deleteAll()
            speedQueries.deleteAll()
            speedValueQueries.deleteAll()
            monsterQueries.deleteAll()
            reactionQueries.deleteAll()
            spellcastingQueries.deleteAll()
            spellcastingSpellUsageCrossRefQueries.deleteAll()
            spellUsageQueries.deleteAll()
            spellUsageCrossRefQueries.deleteAll()
            legendaryActionQueries.deleteAll()
        }
    }

    private fun <T> List<List<T>>.reduceList(): List<T> {
        return this.reduceOrNull { acc, list -> acc + list } ?: emptyList()
    }

    private fun <T, R> List<T>.mapAndReduce(transform: T.() -> List<R>): List<R> {
        return this.map(transform).reduceList()
    }

    private fun List<MonsterDatabaseEntity>.queryMonsterCompleteEntities(): List<MonsterCompleteEntity> =
        map { monster ->
            val speed = getSpeed(monster, speedQueries, speedValueQueries)
            val abilityScores = getAbilityScores(monster, abilityScoreQueries)
            val actions = getActions(monster, actionQueries, damageDiceQueries)
            val reactions = getReactions(monster, reactionQueries)
            val specialAbilities = getSpecialAbilities(monster, specialAbilityQueries)
            val legendaryActions = getLegendaryActions(
                monster, legendaryActionQueries, damageDiceQueries
            )
            val spellcastings = getSpellcastings(
                monster, spellcastingQueries, spellUsageQueries, spellUsageCrossRefQueries
            )
            val savingThrows = getSavingThrows(monster, savingThrowQueries)
            val skills = getSkills(monster, skillQueries)
            val damageVulnerabilities = getDamageVulnerabilities(
                monster, damageVulnerabilityQueries
            )
            val damageResistances = getDamageResistances(monster, damageResistanceQueries)
            val damageImmunities = getDamageImmunities(monster, damageImmunityQueries)
            val conditionImmunities = getConditionImmunities(monster, conditionQueries)

            MonsterCompleteEntity(
                monster = monster.toLocalEntity(),
                speed = speed,
                abilityScores = abilityScores,
                actions = actions,
                reactions = reactions,
                specialAbilities = specialAbilities,
                legendaryActions = legendaryActions,
                spellcastings = spellcastings,
                savingThrows = savingThrows,
                skills = skills,
                damageVulnerabilities = damageVulnerabilities,
                damageResistances = damageResistances,
                damageImmunities = damageImmunities,
                conditionImmunities = conditionImmunities
            )
        }
}

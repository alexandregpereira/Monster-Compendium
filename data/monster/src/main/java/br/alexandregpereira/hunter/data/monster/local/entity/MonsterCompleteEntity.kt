/*
 * Copyright (C) 2022 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data.monster.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingCompleteEntity
import br.alexandregpereira.hunter.data.monster.spell.local.model.SpellcastingEntity

data class MonsterCompleteEntity(
    @Embedded val monster: MonsterEntity,
    @Relation(
        entity = SpeedEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val speed: SpeedWithValuesEntity?,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val abilityScores: List<AbilityScoreEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val savingThrows: List<SavingThrowEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val skills: List<SkillEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageVulnerabilities: List<DamageVulnerabilityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageResistances: List<DamageResistanceEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val damageImmunities: List<DamageImmunityEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val conditionImmunities: List<ConditionEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val specialAbilities: List<SpecialAbilityEntity>,
    @Relation(
        entity = ActionEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val actions: List<ActionWithDamageDicesEntity>,
    @Relation(
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val reactions: List<ReactionEntity>,
    @Relation(
        entity = SpellcastingEntity::class,
        parentColumn = "index",
        entityColumn = "monsterIndex",
    )
    val spellcastings: List<SpellcastingCompleteEntity>,
)

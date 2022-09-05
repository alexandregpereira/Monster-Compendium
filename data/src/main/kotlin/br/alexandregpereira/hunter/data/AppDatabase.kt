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

package br.alexandregpereira.hunter.data

import androidx.room.Database
import androidx.room.RoomDatabase
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
import br.alexandregpereira.hunter.data.local.entity.AbilityScoreEntity
import br.alexandregpereira.hunter.data.local.entity.ActionEntity
import br.alexandregpereira.hunter.data.local.entity.ConditionEntity
import br.alexandregpereira.hunter.data.local.entity.DamageDiceEntity
import br.alexandregpereira.hunter.data.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.local.entity.MonsterEntity
import br.alexandregpereira.hunter.data.local.entity.ReactionEntity
import br.alexandregpereira.hunter.data.local.entity.SavingThrowEntity
import br.alexandregpereira.hunter.data.local.entity.SkillEntity
import br.alexandregpereira.hunter.data.local.entity.SpecialAbilityEntity
import br.alexandregpereira.hunter.data.local.entity.SpeedEntity
import br.alexandregpereira.hunter.data.local.entity.SpeedValueEntity

@Database(
    entities = [
        AbilityScoreEntity::class,
        ActionEntity::class,
        ConditionEntity::class,
        DamageVulnerabilityEntity::class,
        DamageResistanceEntity::class,
        DamageImmunityEntity::class,
        DamageDiceEntity::class,
        MonsterEntity::class,
        SavingThrowEntity::class,
        SkillEntity::class,
        SpecialAbilityEntity::class,
        SpeedEntity::class,
        SpeedValueEntity::class,
        ReactionEntity::class,
    ],
    version = 10,
    exportSchema = false
)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun abilityScoreDao(): AbilityScoreDao
    abstract fun actionDao(): ActionDao
    abstract fun conditionDao(): ConditionDao
    abstract fun damageDao(): DamageDao
    abstract fun damageDiceDao(): DamageDiceDao
    abstract fun monsterDao(): MonsterDao
    abstract fun savingThrowDao(): SavingThrowDao
    abstract fun skillDao(): SkillDao
    abstract fun specialAbilityDao(): SpecialAbilityDao
    abstract fun speedDao(): SpeedDao
    abstract fun speedValueDao(): SpeedValueDao
    abstract fun reactionDao(): ReactionDao
}
